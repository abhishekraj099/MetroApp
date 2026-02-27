package com.example.metro.data.model

data class Station(
    val id: String,
    val name: String,
    val nameHindi: String = "",
    val corridor: Int,          // 1 = Corridor 1 (Blue), 2 = Corridor 2 (Red)
    val index: Int,             // order in the corridor
    val latitude: Double,
    val longitude: Double,
    val isInterchange: Boolean = false,
    val facilities: List<String> = emptyList(),
    val lines: List<String> = emptyList(),   // e.g. ["RED"], ["BLUE"], ["RED", "BLUE"]
    val nextTrainMin: Int = 2               // simulated ETA in minutes
)

