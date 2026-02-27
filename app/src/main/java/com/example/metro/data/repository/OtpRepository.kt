package com.example.metro.data.repository

import com.example.metro.data.local.SessionDataStore
import com.example.metro.data.remote.OtpApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OtpRepository(
    private val apiService: OtpApiService,
    private val sessionDataStore: SessionDataStore
) {

    val loggedInEmail: Flow<String?> = sessionDataStore.loggedInEmail

    suspend fun sendOtp(email: String): Result<String> = withContext(Dispatchers.IO) {
        apiService.sendOtp(email)
    }

    suspend fun verifyOtp(email: String, otp: String): Result<String> = withContext(Dispatchers.IO) {
        apiService.verifyOtp(email, otp)
    }

    suspend fun saveLoginSession(email: String) {
        sessionDataStore.saveLoginSession(email)
    }

    suspend fun clearSession() {
        sessionDataStore.clearSession()
    }
}

