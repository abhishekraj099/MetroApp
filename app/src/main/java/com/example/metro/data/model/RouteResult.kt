package com.example.metro.data.model

/**
 * Result of a route search between two metro stations.
 */
data class RouteResult(
    val from: Station,
    val to: Station,
    val path: List<Station>,           // ordered list of stations on the route
    val lines: List<String>,           // lines involved e.g. ["RED"], ["RED", "BLUE"]
    val interchangeAt: String? = null, // station name where interchange happens (if any)
    val stationCount: Int,
    val estimatedTimeMin: Int,         // approx. 2 min per station + 5 min interchange
    val fare: Int                      // fare in ₹
)

