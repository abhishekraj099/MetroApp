package com.example.metro.data.model

/**
 * Represents a saved route between two stations.
 */
data class SavedRoute(
    val id: String = "",
    val fromStation: String = "",
    val toStation: String = "",
    val lineName: String = "",
    val lineColorHex: Long = 0xFFE65142,  // default Vermilion Red
    val fare: Int = 0,
    val stationCount: Int = 0,
    val estimatedTime: Int = 0,  // in minutes
    val savedAt: Long = System.currentTimeMillis()
)

