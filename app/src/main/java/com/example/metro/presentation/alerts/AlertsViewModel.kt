package com.example.metro.presentation.alerts

import androidx.lifecycle.ViewModel
import com.example.metro.data.model.MetroAlert
import com.example.metro.data.repository.AlertsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ─── Tab ──────────────────────────────────────────────────────────────────────

enum class AlertTab(val label: String) {
    SERVICE_STATUS("Service Status"),
    PLANNED_WORKS("Planned Works")
}

// ─── UI State ─────────────────────────────────────────────────────────────────

data class AlertsUiState(
    val activeTab: AlertTab = AlertTab.SERVICE_STATUS,
    val serviceAlerts: List<MetroAlert> = emptyList(),
    val plannedAlerts: List<MetroAlert> = emptyList(),
    val expandedAlertId: String? = null
)

// ─── ViewModel ────────────────────────────────────────────────────────────────

class AlertsViewModel(
    private val repository: AlertsRepository = AlertsRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlertsUiState())
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    init {
        loadAlerts()
    }

    private fun loadAlerts() {
        _uiState.update {
            it.copy(
                serviceAlerts = repository.getServiceInfoAlerts(),
                plannedAlerts = repository.getPlannedWorksAlerts()
            )
        }
    }

    fun onTabChange(tab: AlertTab) {
        _uiState.update { it.copy(activeTab = tab, expandedAlertId = null) }
    }

    fun onAlertTap(alertId: String) {
        _uiState.update {
            it.copy(
                expandedAlertId = if (it.expandedAlertId == alertId) null else alertId
            )
        }
    }

    fun currentAlerts(): List<MetroAlert> {
        val state = _uiState.value
        return when (state.activeTab) {
            AlertTab.SERVICE_STATUS -> state.serviceAlerts
            AlertTab.PLANNED_WORKS  -> state.plannedAlerts
        }
    }
}

