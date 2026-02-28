package com.example.metro.data.model

data class Feedback(
    val userId: String = "",
    val email: String = "",
    val message: String = "",
    val rating: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val appVersion: String = "1.0.0",
    val deviceInfo: String = ""
)

