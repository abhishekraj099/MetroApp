package com.example.metro.domain.usecase

import com.example.metro.data.repository.OtpRepository

class SendOtpUseCase(private val repository: OtpRepository) {
    suspend operator fun invoke(email: String): Result<String> = repository.sendOtp(email)
}

