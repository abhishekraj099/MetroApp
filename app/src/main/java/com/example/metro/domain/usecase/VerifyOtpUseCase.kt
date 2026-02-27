package com.example.metro.domain.usecase

import com.example.metro.data.repository.OtpRepository

class VerifyOtpUseCase(private val repository: OtpRepository) {
    suspend operator fun invoke(email: String, otp: String): Result<String> =
        repository.verifyOtp(email, otp)
}

