package com.example.metro.data.repository

import com.example.metro.data.model.MetroLine
import com.example.metro.data.model.RouteResult
import com.example.metro.data.model.ServiceLevel
import com.example.metro.data.model.ServiceStatus
import com.example.metro.data.model.Station
import com.example.metro.ui.theme.IndigoBlue
import com.example.metro.ui.theme.VermilionRed

/**
 * Provides Patna Metro corridor & station data.
 *
 * Corridor 1 (Red)  — Danapur ↔ Mithapur Bus Stand  (East-West)
 * Corridor 2 (Blue) — Patna Junction ↔ New ISBT      (North-South)
 *
 * Coordinates are approximate for map plotting.
 * Hindi names sourced from PMRC official documents.
 */
class MetroRepository {

    fun getCorridor1(): MetroLine {
        val stations = listOf(
            Station("c1_01", "Danapur",            "दानापुर",         1, 0,  25.6221, 85.0490,
                facilities = listOf("Parking", "Lift"), lines = listOf("RED")),
            Station("c1_02", "Saguna More",        "सगुना मोड़",       1, 1,  25.6165, 85.0655,
                facilities = listOf("Parking"), lines = listOf("RED")),
            Station("c1_03", "RPS More",           "आरपीएस मोड़",     1, 2,  25.6130, 85.0830,
                lines = listOf("RED")),
            Station("c1_04", "Rukanpura",          "रुकनपुरा",        1, 3,  25.6108, 85.0970,
                lines = listOf("RED")),
            Station("c1_05", "Raja Bazar",         "राजा बाज़ार",      1, 4,  25.6098, 85.1060,
                lines = listOf("RED")),
            Station("c1_06", "Patna Junction",     "पटना जंक्शन",     1, 5,  25.6075, 85.1200,
                isInterchange = true, facilities = listOf("Parking", "Lift", "Wheelchair"),
                lines = listOf("RED", "BLUE"), nextTrainMin = 2),
            Station("c1_07", "Frazer Road",        "फ्रेजर रोड",       1, 6,  25.6120, 85.1290,
                facilities = listOf("Lift"), lines = listOf("RED"), nextTrainMin = 2),
            Station("c1_08", "Gandhi Maidan",      "गांधी मैदान",      1, 7,  25.6155, 85.1350,
                facilities = listOf("Lift"), lines = listOf("RED"), nextTrainMin = 2),
            Station("c1_09", "PMCH",               "पीएमसीएच",        1, 8,  25.6100, 85.1410,
                facilities = listOf("Lift"), lines = listOf("RED"), nextTrainMin = 2),
            Station("c1_10", "Rajendra Nagar",     "राजेन्द्र नगर",    1, 9,  25.6050, 85.1460,
                facilities = listOf("Parking"), lines = listOf("RED"), nextTrainMin = 3),
            Station("c1_11", "Mithapur Bus Stand", "मीठापुर बस स्टैंड", 1, 10, 25.6010, 85.1520,
                facilities = listOf("Parking"), lines = listOf("RED"), nextTrainMin = 3)
        )
        return MetroLine(id = 1, name = "Corridor 1", color = VermilionRed, stations = stations)
    }

    fun getCorridor2(): MetroLine {
        val stations = listOf(
            Station("c2_01", "Patna Junction",     "पटना जंक्शन",     2, 0, 25.6075, 85.1200,
                isInterchange = true, facilities = listOf("Parking", "Lift", "Wheelchair"),
                lines = listOf("RED", "BLUE"), nextTrainMin = 2),
            Station("c2_02", "Akashvani",          "आकाशवाणी",        2, 1, 25.6130, 85.1250,
                lines = listOf("BLUE"), nextTrainMin = 3),
            Station("c2_03", "Secretariat",        "सचिवालय",         2, 2, 25.6175, 85.1310,
                lines = listOf("BLUE"), nextTrainMin = 3),
            Station("c2_04", "Vidyut Bhawan",      "विद्युत भवन",      2, 3, 25.6220, 85.1380,
                lines = listOf("BLUE"), nextTrainMin = 4),
            Station("c2_05", "Shri Krishna Nagar", "श्री कृष्ण नगर",   2, 4, 25.6265, 85.1450,
                lines = listOf("BLUE"), nextTrainMin = 4),
            Station("c2_06", "Patliputra",         "पाटलिपुत्र",       2, 5, 25.6310, 85.1530,
                facilities = listOf("Parking", "Lift"), lines = listOf("BLUE"), nextTrainMin = 5),
            Station("c2_07", "Jaganpura",          "जगनपुरा",         2, 6, 25.6360, 85.1620,
                lines = listOf("BLUE"), nextTrainMin = 5),
            Station("c2_08", "Khemnichak",         "खेमनीचक",         2, 7, 25.6400, 85.1720,
                lines = listOf("BLUE"), nextTrainMin = 6),
            Station("c2_09", "New ISBT",           "नया आईएसबीटी",    2, 8, 25.6450, 85.1830,
                facilities = listOf("Parking", "Lift"), lines = listOf("BLUE"), nextTrainMin = 7)
        )
        return MetroLine(id = 2, name = "Corridor 2", color = IndigoBlue, stations = stations)
    }

    fun getAllLines(): List<MetroLine> = listOf(getCorridor1(), getCorridor2())

    fun getAllStations(): List<Station> {
        val all = mutableListOf<Station>()
        getCorridor1().stations.forEach { all.add(it) }
        getCorridor2().stations.forEach { s ->
            if (all.none { it.name == s.name }) all.add(s)
        }
        return all
    }

    fun getStationsByLine(line: String): List<Station> {
        return getAllStations().filter { line in it.lines }
    }

    fun searchStations(query: String): List<Station> {
        if (query.isBlank()) return getAllStations()
        val q = query.lowercase().trim()
        return getAllStations().filter {
            it.name.lowercase().contains(q) || it.nameHindi.contains(q)
        }
    }

    fun getStationByName(name: String): Station? =
        getAllStations().find { it.name.equals(name, ignoreCase = true) }

    /**
     * Returns all station names for dropdown/autocomplete.
     */
    fun getStationNames(): List<String> = getAllStations().map { it.name }

    /**
     * Finds route between two stations.
     * Handles same-line and cross-line (via interchange at Patna Junction).
     */
    fun findRoute(fromName: String, toName: String): RouteResult? {
        val from = getStationByName(fromName) ?: return null
        val to = getStationByName(toName) ?: return null
        if (from.name == to.name) return null

        val corridor1 = getCorridor1().stations
        val corridor2 = getCorridor2().stations

        val fromOnRed = corridor1.find { it.name == from.name }
        val toOnRed = corridor1.find { it.name == to.name }
        val fromOnBlue = corridor2.find { it.name == from.name }
        val toOnBlue = corridor2.find { it.name == to.name }

        // Case 1: Both on Red line
        if (fromOnRed != null && toOnRed != null) {
            val path = extractPath(corridor1, fromOnRed, toOnRed)
            return buildResult(from, to, path, listOf("RED"))
        }

        // Case 2: Both on Blue line
        if (fromOnBlue != null && toOnBlue != null) {
            val path = extractPath(corridor2, fromOnBlue, toOnBlue)
            return buildResult(from, to, path, listOf("BLUE"))
        }

        // Case 3: Cross-line — interchange at Patna Junction
        val junction = "Patna Junction"
        val junctionOnRed = corridor1.find { it.name == junction }!!
        val junctionOnBlue = corridor2.find { it.name == junction }!!

        val pathPart1: List<Station>
        val pathPart2: List<Station>
        val lines: List<String>

        if (fromOnRed != null && toOnBlue != null) {
            pathPart1 = extractPath(corridor1, fromOnRed, junctionOnRed)
            pathPart2 = extractPath(corridor2, junctionOnBlue, toOnBlue).drop(1) // skip duplicate junction
            lines = listOf("RED", "BLUE")
        } else if (fromOnBlue != null && toOnRed != null) {
            pathPart1 = extractPath(corridor2, fromOnBlue, junctionOnBlue)
            pathPart2 = extractPath(corridor1, junctionOnRed, toOnRed).drop(1)
            lines = listOf("BLUE", "RED")
        } else {
            return null
        }

        val fullPath = pathPart1 + pathPart2
        return buildResult(from, to, fullPath, lines, interchangeAt = junction)
    }

    private fun extractPath(corridor: List<Station>, from: Station, to: Station): List<Station> {
        val fromIdx = corridor.indexOfFirst { it.name == from.name }
        val toIdx = corridor.indexOfFirst { it.name == to.name }
        return if (fromIdx <= toIdx) {
            corridor.subList(fromIdx, toIdx + 1)
        } else {
            corridor.subList(toIdx, fromIdx + 1).reversed()
        }
    }

    private fun buildResult(
        from: Station,
        to: Station,
        path: List<Station>,
        lines: List<String>,
        interchangeAt: String? = null
    ): RouteResult {
        val stationCount = path.size
        val interchangeTime = if (interchangeAt != null) 5 else 0
        val estimatedTime = (stationCount - 1) * 2 + interchangeTime  // 2 min per station gap
        val fare = calculateFare(stationCount)
        return RouteResult(from, to, path, lines, interchangeAt, stationCount, estimatedTime, fare)
    }

    /**
     * Fare calculation based on Patna Metro fare structure (approximate).
     * Base fare ₹10, increases every few stations.
     */
    fun calculateFare(stationCount: Int): Int {
        return when {
            stationCount <= 2  -> 10
            stationCount <= 5  -> 15
            stationCount <= 8  -> 20
            stationCount <= 12 -> 25
            stationCount <= 16 -> 30
            else               -> 35
        }
    }

    /**
     * Returns current service status for all lines.
     */
    fun getServiceStatus(): List<ServiceStatus> {
        // Since this is an unofficial app, we show default "Normal" status.
        // In production, this would fetch from an API.
        return listOf(
            ServiceStatus(
                lineName = "Red Line (Corridor 1)",
                status = ServiceLevel.NORMAL,
                message = "All services running on time."
            ),
            ServiceStatus(
                lineName = "Blue Line (Corridor 2)",
                status = ServiceLevel.NORMAL,
                message = "All services running on time."
            )
        )
    }

    /**
     * Returns overall service status message.
     */
    fun getOverallServiceStatus(): Pair<ServiceLevel, String> {
        val statuses = getServiceStatus()
        val hasDelay = statuses.any { it.status != ServiceLevel.NORMAL }
        return if (hasDelay) {
            Pair(ServiceLevel.MINOR_DELAY, "Some delays reported on the network.")
        } else {
            Pair(ServiceLevel.NORMAL, "All lines running smoothly today.")
        }
    }
}

