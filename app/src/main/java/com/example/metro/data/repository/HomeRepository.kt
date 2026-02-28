package com.example.metro.data.repository

import com.example.metro.data.local.SavedRoutesDataStore
import com.example.metro.data.model.RouteResult
import com.example.metro.data.model.SavedRoute
import com.example.metro.data.model.ServiceLevel
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Home screen repository — combines metro data with local saved routes.
 */
class HomeRepository(
    private val metroRepository: MetroRepository = MetroRepository(),
    private val savedRoutesDataStore: SavedRoutesDataStore
) {

    // ── Station Data ────────────────────────────────────────────────────────

    fun getStationNames(): List<String> = metroRepository.getStationNames()

    // ── Route Finding ───────────────────────────────────────────────────────

    fun findRoute(from: String, to: String): RouteResult? = metroRepository.findRoute(from, to)

    // ── Saved Routes (persisted locally) ────────────────────────────────────

    val savedRoutes: Flow<List<SavedRoute>> = savedRoutesDataStore.savedRoutes

    suspend fun saveRouteFromResult(result: RouteResult) {
        val lineName = when {
            result.lines.size == 1 && result.lines[0] == "RED"  -> "Red Line"
            result.lines.size == 1 && result.lines[0] == "BLUE" -> "Blue Line"
            else -> "Red + Blue"
        }
        val lineColor = when {
            result.lines.size == 1 && result.lines[0] == "BLUE" -> 0xFF34437A
            else -> 0xFFE65142
        }
        val route = SavedRoute(
            id = UUID.randomUUID().toString(),
            fromStation = result.from.name,
            toStation = result.to.name,
            lineName = lineName,
            lineColorHex = lineColor,
            fare = result.fare,
            stationCount = result.stationCount,
            estimatedTime = result.estimatedTimeMin,
            savedAt = System.currentTimeMillis()
        )
        savedRoutesDataStore.saveRoute(route)
    }

    suspend fun removeRoute(routeId: String) {
        savedRoutesDataStore.removeRoute(routeId)
    }

    // ── Service Status ──────────────────────────────────────────────────────

    fun getOverallServiceStatus(): Pair<ServiceLevel, String> =
        metroRepository.getOverallServiceStatus()
}

