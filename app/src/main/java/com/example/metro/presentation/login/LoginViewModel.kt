package com.example.metro.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.metro.data.local.SessionDataStore
import com.example.metro.data.model.User
import com.example.metro.data.remote.OtpApiService
import com.example.metro.data.repository.FirebaseUserRepository
import com.example.metro.data.repository.OtpRepository
import com.example.metro.domain.usecase.SendOtpUseCase
import com.example.metro.domain.usecase.VerifyOtpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ─── UI State ─────────────────────────────────────────────────────────────────

data class LoginUiState(
    val email: String = "",
    val emailError: String = "",
    val otp: String = "",
    val showOtpField: Boolean = false,
    val isLoading: Boolean = false,
    val isVerifying: Boolean = false,
    val message: String = "",
    val isLoggedIn: Boolean = false,
    val loggedInUser: User? = null
)

// ─── ViewModel ────────────────────────────────────────────────────────────────

class LoginViewModel(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val repository: OtpRepository,
    private val firebaseUserRepository: FirebaseUserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // ── Input handlers ──────────────────────────────────────────────────────

    fun onEmailChange(value: String) {
        val trimmed = value.lowercase().trim()
        _uiState.update {
            it.copy(
                email = trimmed,
                emailError = if (trimmed.isNotEmpty() && !isValidEmail(trimmed))
                    "Please enter a valid Gmail address" else ""
            )
        }
    }

    fun onOtpChange(value: String) {
        if (value.length <= 6 && value.all { it.isDigit() }) {
            _uiState.update { it.copy(otp = value) }
        }
    }

    fun onChangeEmailClicked() {
        _uiState.update { it.copy(showOtpField = false, otp = "", message = "") }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = "") }
    }

    // ── Actions ─────────────────────────────────────────────────────────────

    fun sendOtp() {
        val email = _uiState.value.email
        if (!isValidEmail(email)) {
            _uiState.update { it.copy(message = "Please enter a valid Gmail address") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = "") }
            val result = sendOtpUseCase(email)
            _uiState.update { state ->
                if (result.isSuccess) {
                    state.copy(isLoading = false, showOtpField = true, message = "OTP sent to your email ✅")
                } else {
                    state.copy(isLoading = false, message = result.exceptionOrNull()?.message ?: "Failed to send OTP")
                }
            }
        }
    }

    fun verifyOtp() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isVerifying = true, message = "") }
            val result = verifyOtpUseCase(state.email, state.otp)
            if (result.isSuccess) {
                // ── Save session locally ─────────────────────────────────
                repository.saveLoginSession(state.email)

                // ── Firebase: check if user already exists ───────────────
                val (exists, existingUser) = firebaseUserRepository.checkUserExists(state.email)

                val user: User
                val welcomeMsg: String

                if (exists && existingUser != null) {
                    // Returning user — update lastLogin status in Firebase
                    firebaseUserRepository.updateUserFields(
                        state.email,
                        mapOf("status" to "online", "lastLogin" to System.currentTimeMillis())
                    )
                    user = existingUser
                    welcomeMsg = "Welcome back! 🎉"
                } else {
                    // New user — create profile in Firebase
                    user = User(
                        userId = state.email.replace(".", "_").replace("@", "_at_"),
                        email = state.email,
                        status = "online",
                        createdAt = System.currentTimeMillis()
                    )
                    firebaseUserRepository.saveNewUser(user)
                    welcomeMsg = "Welcome! 🎉"
                }

                _uiState.update {
                    it.copy(
                        isVerifying = false,
                        message = welcomeMsg,
                        isLoggedIn = true,
                        loggedInUser = user
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isVerifying = false,
                        message = result.exceptionOrNull()?.message ?: "Invalid OTP"
                    )
                }
            }
        }
    }

    // ── Validation ───────────────────────────────────────────────────────────

    fun isValidEmail(email: String): Boolean =
        email.matches("[a-zA-Z0-9._-]+@gmail\\.com".toRegex())

    // ── Factory ─────────────────────────────────────────────────────────────

    class Factory(private val sessionDataStore: SessionDataStore) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val api = OtpApiService()
            val repo = OtpRepository(api, sessionDataStore)
            val firebaseRepo = FirebaseUserRepository()
            return LoginViewModel(
                SendOtpUseCase(repo),
                VerifyOtpUseCase(repo),
                repo,
                firebaseRepo
            ) as T
        }
    }
}
