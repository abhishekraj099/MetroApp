package com.example.metro.presentation.explore

import androidx.lifecycle.ViewModel
import com.example.metro.data.model.PatnaPlace
import com.example.metro.data.model.PlaceCategory
import com.example.metro.data.repository.PlacesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ─── UI State ─────────────────────────────────────────────────────────────────

data class ExploreUiState(
    val allPlaces: List<PatnaPlace> = emptyList(),
    val filteredPlaces: List<PatnaPlace> = emptyList(),
    val expandedPlaceId: String? = null,
    val searchQuery: String = "",
    val selectedCategory: PlaceCategory? = null   // null = All
)

// ─── ViewModel ────────────────────────────────────────────────────────────────

class ExploreViewModel(
    private val repository: PlacesRepository = PlacesRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        val places = repository.getAllPlaces()
        _uiState.update { it.copy(allPlaces = places, filteredPlaces = places) }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state ->
            val filtered = applyFilters(query, state.selectedCategory)
            state.copy(searchQuery = query, filteredPlaces = filtered)
        }
    }

    fun onCategoryChange(category: PlaceCategory?) {
        _uiState.update { state ->
            val filtered = applyFilters(state.searchQuery, category)
            state.copy(selectedCategory = category, filteredPlaces = filtered)
        }
    }

    fun onPlaceTap(placeId: String) {
        _uiState.update {
            it.copy(expandedPlaceId = if (it.expandedPlaceId == placeId) null else placeId)
        }
    }

    private fun applyFilters(query: String, category: PlaceCategory?): List<PatnaPlace> {
        var result = repository.getAllPlaces()
        if (category != null) {
            result = result.filter { it.category == category }
        }
        if (query.isNotBlank()) {
            val q = query.lowercase().trim()
            result = result.filter {
                it.name.lowercase().contains(q) ||
                it.nameHindi.contains(q) ||
                it.nearestStation.lowercase().contains(q)
            }
        }
        return result
    }
}

