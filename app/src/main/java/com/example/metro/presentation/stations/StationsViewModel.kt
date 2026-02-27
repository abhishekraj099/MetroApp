package com.example.metro.presentation.stations

import androidx.lifecycle.ViewModel
import com.example.metro.data.model.Station
import com.example.metro.data.repository.MetroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ─── Filter Tab ───────────────────────────────────────────────────────────────

enum class LineFilter(val label: String) {
    ALL("All Lines"),
    RED("Red Line"),
    BLUE("Blue Line")
}

// ─── UI State ─────────────────────────────────────────────────────────────────

data class StationsUiState(
    val allStations: List<Station> = emptyList(),
    val filteredStations: List<Station> = emptyList(),
    val searchQuery: String = "",
    val activeFilter: LineFilter = LineFilter.ALL,
    val selectedStation: Station? = null
)

// ─── ViewModel ────────────────────────────────────────────────────────────────

class StationsViewModel(
    private val repository: MetroRepository = MetroRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(StationsUiState())
    val uiState: StateFlow<StationsUiState> = _uiState.asStateFlow()

    init {
        loadStations()
    }

    private fun loadStations() {
        val stations = repository.getAllStations()
        _uiState.update {
            it.copy(allStations = stations, filteredStations = stations)
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state ->
            val filtered = applyFilters(query, state.activeFilter)
            state.copy(searchQuery = query, filteredStations = filtered)
        }
    }

    fun onFilterChange(filter: LineFilter) {
        _uiState.update { state ->
            val filtered = applyFilters(state.searchQuery, filter)
            state.copy(activeFilter = filter, filteredStations = filtered)
        }
    }

    fun onStationSelected(station: Station) {
        _uiState.update {
            if (it.selectedStation?.id == station.id) {
                it.copy(selectedStation = null)
            } else {
                it.copy(selectedStation = station)
            }
        }
    }

    fun dismissStationDetail() {
        _uiState.update { it.copy(selectedStation = null) }
    }

    private fun applyFilters(query: String, filter: LineFilter): List<Station> {
        val byLine = when (filter) {
            LineFilter.ALL  -> repository.getAllStations()
            LineFilter.RED  -> repository.getStationsByLine("RED")
            LineFilter.BLUE -> repository.getStationsByLine("BLUE")
        }
        if (query.isBlank()) return byLine
        val q = query.lowercase().trim()
        return byLine.filter {
            it.name.lowercase().contains(q) || it.nameHindi.contains(q)
        }
    }
}

