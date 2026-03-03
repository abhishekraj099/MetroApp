package com.example.metro.presentation.map

import androidx.lifecycle.ViewModel
import com.example.metro.data.model.MetroLine
import com.example.metro.data.model.Station
import com.example.metro.data.repository.MetroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ─── UI State ─────────────────────────────────────────────────────────────────

data class MapUiState(
    val lines: List<MetroLine> = emptyList(),
    val selectedStation: Station? = null,
    val zoomLevel: Float = 1f,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val showCorridor1: Boolean = true,
    val showCorridor2: Boolean = true
)

// ─── ViewModel ────────────────────────────────────────────────────────────────

class MapViewModel(
    private val repository: MetroRepository = MetroRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadLines()
    }

    private fun loadLines() {
        val lines = repository.getAllLines()
        _uiState.update { it.copy(lines = lines) }
    }

    fun onStationTapped(station: Station) {
        _uiState.update {
            // Toggle: tap same station again → dismiss
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

    fun zoomIn() {
        _uiState.update {
            it.copy(zoomLevel = (it.zoomLevel + 0.25f).coerceAtMost(3f))
        }
    }

    fun zoomOut() {
        _uiState.update {
            it.copy(zoomLevel = (it.zoomLevel - 0.25f).coerceAtLeast(0.5f))
        }
    }

    fun resetView() {
        _uiState.update {
            it.copy(zoomLevel = 1f, offsetX = 0f, offsetY = 0f, selectedStation = null)
        }
    }

    fun onPan(deltaX: Float, deltaY: Float) {
        _uiState.update {
            it.copy(
                offsetX = it.offsetX + deltaX,
                offsetY = it.offsetY + deltaY
            )
        }
    }

    /** Called by transformable — multiplies zoom by pinch scale factor */
    fun onZoomChange(scaleFactor: Float) {
        _uiState.update {
            it.copy(zoomLevel = (it.zoomLevel * scaleFactor).coerceIn(0.4f, 4f))
        }
    }

    /** Called by transformable — adds pan delta */
    fun onPanChange(dx: Float, dy: Float) {
        _uiState.update {
            it.copy(offsetX = it.offsetX + dx, offsetY = it.offsetY + dy)
        }
    }

    fun toggleCorridor1() {
        _uiState.update { it.copy(showCorridor1 = !it.showCorridor1) }
    }

    fun toggleCorridor2() {
        _uiState.update { it.copy(showCorridor2 = !it.showCorridor2) }
    }
}
