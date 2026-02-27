package com.example.metro.data.model

/**
 * Represents a tourist place / point of interest near Patna Metro stations.
 */
data class PatnaPlace(
    val id: String,
    val name: String,
    val nameHindi: String,
    val description: String,
    val nearestStation: String,
    val distance: String,           // e.g. "500m from Gandhi Maidan station"
    val timings: String,
    val entryFee: String,
    val category: PlaceCategory,
    val highlights: List<String>,
    val emoji: String               // emoji icon as placeholder until real images are added
)

enum class PlaceCategory(val label: String) {
    HERITAGE("Heritage"),
    RELIGIOUS("Religious"),
    NATURE("Nature"),
    MUSEUM("Museum"),
    LANDMARK("Landmark")
}

