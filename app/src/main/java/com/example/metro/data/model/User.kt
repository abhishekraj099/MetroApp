package com.example.metro.data.model

data class User(
    val userId: String = "",       // email with dots replaced by underscores
    val email: String = "",
    val status: String = "online",
    val createdAt: Long = System.currentTimeMillis()
)

