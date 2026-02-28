package com.example.metro.presentation.settings

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.metro.data.local.SessionDataStore
import com.example.metro.data.model.Feedback
import com.example.metro.data.repository.FeedbackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ─── UI State ─────────────────────────────────────────────────────────────────

data class SettingsUiState(
    val userEmail: String = "",

    // Feedback
    val showFeedbackDialog: Boolean = false,
    val feedbackMessage: String = "",
    val feedbackRating: Int = 0,
    val isSendingFeedback: Boolean = false,
    val feedbackSent: Boolean = false,
    val feedbackError: String = "",

    // Logout
    val showLogoutConfirm: Boolean = false,
    val isLoggingOut: Boolean = false
)

// ─── ViewModel ────────────────────────────────────────────────────────────────

class SettingsViewModel(
    private val sessionDataStore: SessionDataStore,
    private val feedbackRepository: FeedbackRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeSession()
    }

    private fun observeSession() {
        viewModelScope.launch {
            sessionDataStore.loggedInEmail.collect { email ->
                _uiState.update { it.copy(userEmail = email ?: "") }
            }
        }
    }

    // ── Feedback ──────────────────────────────────────────────────────────────

    fun openFeedbackDialog() {
        _uiState.update {
            it.copy(
                showFeedbackDialog = true,
                feedbackMessage = "",
                feedbackRating = 0,
                feedbackSent = false,
                feedbackError = ""
            )
        }
    }

    fun dismissFeedbackDialog() {
        _uiState.update { it.copy(showFeedbackDialog = false) }
    }

    fun onFeedbackMessageChange(msg: String) {
        _uiState.update { it.copy(feedbackMessage = msg) }
    }

    fun onFeedbackRatingChange(rating: Int) {
        _uiState.update { it.copy(feedbackRating = rating) }
    }

    fun submitFeedback() {
        val state = _uiState.value
        if (state.feedbackMessage.isBlank()) {
            _uiState.update { it.copy(feedbackError = "Please write your feedback") }
            return
        }
        if (state.feedbackRating == 0) {
            _uiState.update { it.copy(feedbackError = "Please select a rating") }
            return
        }

        _uiState.update { it.copy(isSendingFeedback = true, feedbackError = "") }

        viewModelScope.launch {
            val feedback = Feedback(
                userId = state.userEmail.toUserId(),
                email = state.userEmail,
                message = state.feedbackMessage.trim(),
                rating = state.feedbackRating,
                appVersion = "1.0.0",
                deviceInfo = "${Build.MANUFACTURER} ${Build.MODEL} | Android ${Build.VERSION.RELEASE}"
            )

            val success = feedbackRepository.submitFeedback(feedback)
            _uiState.update {
                if (success) {
                    it.copy(
                        isSendingFeedback = false,
                        feedbackSent = true,
                        feedbackMessage = "",
                        feedbackRating = 0
                    )
                } else {
                    it.copy(
                        isSendingFeedback = false,
                        feedbackError = "Failed to submit. Check your connection."
                    )
                }
            }
        }
    }

    // ── Logout ────────────────────────────────────────────────────────────────

    fun showLogoutConfirmation() {
        _uiState.update { it.copy(showLogoutConfirm = true) }
    }

    fun dismissLogoutConfirmation() {
        _uiState.update { it.copy(showLogoutConfirm = false) }
    }

    fun logout(onLoggedOut: () -> Unit) {
        _uiState.update { it.copy(isLoggingOut = true) }
        viewModelScope.launch {
            sessionDataStore.clearSession()
            _uiState.update { it.copy(isLoggingOut = false, showLogoutConfirm = false) }
            onLoggedOut()
        }
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun String.toUserId(): String = replace(".", "_").replace("@", "_at_")

    // ── Factory ───────────────────────────────────────────────────────────────

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val sessionStore = SessionDataStore(context)
            val feedbackRepo = FeedbackRepository()
            return SettingsViewModel(sessionStore, feedbackRepo) as T
        }
    }
}

