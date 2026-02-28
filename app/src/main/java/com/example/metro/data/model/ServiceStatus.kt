package com.example.metro.data.model

/**
 * Represents the current service status of a metro line.
 */
data class ServiceStatus(
    val lineName: String,
    val status: ServiceLevel,
    val message: String
)

enum class ServiceLevel {
    NORMAL,       // All clear
    MINOR_DELAY,  // Minor delays
    MAJOR_DELAY,  // Significant delays
    SUSPENDED     // Service suspended
}

