package com.example.metro.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.metro.data.model.SavedRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.savedRoutesStore: DataStore<Preferences> by preferencesDataStore(name = "saved_routes")

/**
 * Persists saved routes using DataStore + JSON serialization.
 */
class SavedRoutesDataStore(private val context: Context) {

    companion object {
        private val KEY_ROUTES = stringPreferencesKey("routes_json")
    }

    val savedRoutes: Flow<List<SavedRoute>> = context.savedRoutesStore.data.map { prefs ->
        val json = prefs[KEY_ROUTES] ?: "[]"
        parseRoutes(json)
    }

    suspend fun saveRoute(route: SavedRoute) {
        context.savedRoutesStore.edit { prefs ->
            val existing = parseRoutes(prefs[KEY_ROUTES] ?: "[]").toMutableList()
            // Don't duplicate same from→to
            existing.removeAll { it.fromStation == route.fromStation && it.toStation == route.toStation }
            existing.add(0, route) // add to front
            // Keep max 10 saved routes
            val limited = existing.take(10)
            prefs[KEY_ROUTES] = toJson(limited)
        }
    }

    suspend fun removeRoute(routeId: String) {
        context.savedRoutesStore.edit { prefs ->
            val existing = parseRoutes(prefs[KEY_ROUTES] ?: "[]").toMutableList()
            existing.removeAll { it.id == routeId }
            prefs[KEY_ROUTES] = toJson(existing)
        }
    }

    suspend fun clearAll() {
        context.savedRoutesStore.edit { prefs ->
            prefs[KEY_ROUTES] = "[]"
        }
    }

    // ── JSON serialization ──────────────────────────────────────────────────

    private fun toJson(routes: List<SavedRoute>): String {
        val arr = JSONArray()
        routes.forEach { r ->
            arr.put(JSONObject().apply {
                put("id", r.id)
                put("fromStation", r.fromStation)
                put("toStation", r.toStation)
                put("lineName", r.lineName)
                put("lineColorHex", r.lineColorHex)
                put("fare", r.fare)
                put("stationCount", r.stationCount)
                put("estimatedTime", r.estimatedTime)
                put("savedAt", r.savedAt)
            })
        }
        return arr.toString()
    }

    private fun parseRoutes(json: String): List<SavedRoute> {
        return try {
            val arr = JSONArray(json)
            (0 until arr.length()).map { i ->
                val obj = arr.getJSONObject(i)
                SavedRoute(
                    id = obj.optString("id"),
                    fromStation = obj.optString("fromStation"),
                    toStation = obj.optString("toStation"),
                    lineName = obj.optString("lineName"),
                    lineColorHex = obj.optLong("lineColorHex", 0xFFE65142),
                    fare = obj.optInt("fare"),
                    stationCount = obj.optInt("stationCount"),
                    estimatedTime = obj.optInt("estimatedTime"),
                    savedAt = obj.optLong("savedAt")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

