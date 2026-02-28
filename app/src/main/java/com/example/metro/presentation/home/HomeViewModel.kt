package com.example.metro.presentation.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.metro.data.local.SavedRoutesDataStore
import com.example.metro.data.local.SessionDataStore
import com.example.metro.data.model.RouteResult
import com.example.metro.data.model.SavedRoute
import com.example.metro.data.model.ServiceLevel
import com.example.metro.data.repository.HomeRepository
import com.example.metro.domain.usecase.FindRouteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ─── UI State ─────────────────────────────────────────────────────────────────

/**
 * Which station field the picker is opened for.
 */
enum class StationPickerTarget { FROM, TO, FARE_FROM, FARE_TO }

enum class QuickActionType { FARE, TIMINGS, NEAREST, ACCESSIBILITY }

data class HomeUiState(
    // Route planner
    val fromStation: String = "",
    val toStation: String = "",
    val stationNames: List<String> = emptyList(),

    // Station picker bottom sheet
    val showStationPicker: Boolean = false,
    val pickerTarget: StationPickerTarget = StationPickerTarget.FROM,
    val pickerQuery: String = "",
    val allStations: List<com.example.metro.data.model.Station> = emptyList(),
    val corridor1Stations: List<com.example.metro.data.model.Station> = emptyList(),
    val corridor2Stations: List<com.example.metro.data.model.Station> = emptyList(),

    // Route result
    val routeResult: RouteResult? = null,
    val showRouteResult: Boolean = false,
    val routeError: String = "",

    // Service status
    val serviceLevel: ServiceLevel = ServiceLevel.NORMAL,
    val serviceMessage: String = "All lines running smoothly today.",

    // Saved routes
    val savedRoutes: List<SavedRoute> = emptyList(),

    // User
    val loggedInEmail: String = "",

    // Quick actions
    val activeQuickAction: QuickActionType? = null,

    // Fare calculator
    val fareFromStation: String = "",
    val fareToStation: String = "",
    val fareResult: Int? = null,
    val fareStationCount: Int? = null
)

// ─── ViewModel ────────────────────────────────────────────────────────────────

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val findRouteUseCase: FindRouteUseCase,
    private val sessionDataStore: SessionDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadStationNames()
        loadServiceStatus()
        observeSavedRoutes()
        observeSession()
    }

    // ── Init loaders ────────────────────────────────────────────────────────

    private fun loadStationNames() {
        val names = homeRepository.getStationNames()
        val stations = homeRepository.getAllStations()
        val c1 = homeRepository.getCorridor1Stations()
        val c2 = homeRepository.getCorridor2Stations()
        _uiState.update {
            it.copy(
                stationNames = names,
                allStations = stations,
                corridor1Stations = c1,
                corridor2Stations = c2
            )
        }
    }

    private fun loadServiceStatus() {
        val (level, message) = homeRepository.getOverallServiceStatus()
        _uiState.update { it.copy(serviceLevel = level, serviceMessage = message) }
    }

    private fun observeSavedRoutes() {
        viewModelScope.launch {
            homeRepository.savedRoutes.collect { routes ->
                _uiState.update { it.copy(savedRoutes = routes) }
            }
        }
    }

    private fun observeSession() {
        viewModelScope.launch {
            sessionDataStore.loggedInEmail.collect { email ->
                _uiState.update { it.copy(loggedInEmail = email ?: "") }
            }
        }
    }

    // ── Station Picker ────────────────────────────────────────────────────

    fun openStationPicker(target: StationPickerTarget) {
        Log.d("HomeViewModel", "openStationPicker called: target=$target, current stations: c1=${_uiState.value.corridor1Stations.size}, c2=${_uiState.value.corridor2Stations.size}")
        _uiState.update {
            it.copy(
                showStationPicker = true,
                pickerTarget = target,
                pickerQuery = ""
            )
        }
        Log.d("HomeViewModel", "showStationPicker is now: ${_uiState.value.showStationPicker}")
    }

    fun onPickerQueryChange(query: String) {
        _uiState.update { it.copy(pickerQuery = query) }
    }

    fun onStationSelected(name: String) {
        _uiState.update {
            when (it.pickerTarget) {
                StationPickerTarget.FROM -> it.copy(
                    fromStation = name,
                    showStationPicker = false,
                    pickerQuery = "",
                    showRouteResult = false,
                    routeError = ""
                )
                StationPickerTarget.TO -> it.copy(
                    toStation = name,
                    showStationPicker = false,
                    pickerQuery = "",
                    showRouteResult = false,
                    routeError = ""
                )
                StationPickerTarget.FARE_FROM -> it.copy(
                    fareFromStation = name,
                    showStationPicker = false,
                    pickerQuery = ""
                )
                StationPickerTarget.FARE_TO -> it.copy(
                    fareToStation = name,
                    showStationPicker = false,
                    pickerQuery = ""
                )
            }
        }
        // Auto-calculate fare when both fare stations are selected
        val state = _uiState.value
        if (state.fareFromStation.isNotBlank() && state.fareToStation.isNotBlank()) {
            calculateFareResult()
        }
    }

    fun dismissStationPicker() {
        _uiState.update { it.copy(showStationPicker = false, pickerQuery = "") }
    }

    // ── Quick Actions ────────────────────────────────────────────────────

    fun onQuickActionOpen(type: QuickActionType) {
        _uiState.update { it.copy(activeQuickAction = type) }
    }

    fun dismissQuickAction() {
        _uiState.update {
            it.copy(
                activeQuickAction = null,
                fareFromStation = "",
                fareToStation = "",
                fareResult = null,
                fareStationCount = null
            )
        }
    }

    fun openFareStationPicker(target: StationPickerTarget) {
        _uiState.update {
            it.copy(
                showStationPicker = true,
                pickerTarget = target,
                pickerQuery = ""
            )
        }
    }

    private fun calculateFareResult() {
        val state = _uiState.value
        val route = homeRepository.findRoute(state.fareFromStation, state.fareToStation)
        if (route != null) {
            _uiState.update {
                it.copy(
                    fareResult = route.fare,
                    fareStationCount = route.stationCount
                )
            }
        }
    }

    // ── From station input (kept for compatibility) ─────────────────────────

    fun onFromStationChange(value: String) {
        _uiState.update {
            it.copy(
                fromStation = value,
                showRouteResult = false,
                routeError = ""
            )
        }
    }

    fun onFromStationSelected(name: String) {
        _uiState.update {
            it.copy(fromStation = name)
        }
    }

    // ── To station input (kept for compatibility) ───────────────────────────

    fun onToStationChange(value: String) {
        _uiState.update {
            it.copy(
                toStation = value,
                showRouteResult = false,
                routeError = ""
            )
        }
    }

    fun onToStationSelected(name: String) {
        _uiState.update {
            it.copy(toStation = name)
        }
    }

    // ── Swap stations ───────────────────────────────────────────────────────

    fun onSwapStations() {
        _uiState.update {
            it.copy(
                fromStation = it.toStation,
                toStation = it.fromStation,
                showRouteResult = false,
                routeError = ""
            )
        }
    }

    // ── Find route ──────────────────────────────────────────────────────────

    fun onFindRoute() {
        val state = _uiState.value
        val result = findRouteUseCase(state.fromStation, state.toStation)

        result.fold(
            onSuccess = { routeResult ->
                _uiState.update {
                    it.copy(
                        routeResult = routeResult,
                        showRouteResult = true,
                        routeError = ""
                    )
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        routeResult = null,
                        showRouteResult = false,
                        routeError = error.message ?: "Route not found"
                    )
                }
            }
        )
    }

    fun dismissRouteResult() {
        _uiState.update { it.copy(showRouteResult = false) }
    }

    // ── Save route ──────────────────────────────────────────────────────────

    fun onSaveCurrentRoute() {
        val result = _uiState.value.routeResult ?: return
        viewModelScope.launch {
            homeRepository.saveRouteFromResult(result)
        }
    }

    fun onRemoveSavedRoute(routeId: String) {
        viewModelScope.launch {
            homeRepository.removeRoute(routeId)
        }
    }

    // ── Load a saved route into the planner ─────────────────────────────────

    fun onLoadSavedRoute(route: SavedRoute) {
        _uiState.update {
            it.copy(
                fromStation = route.fromStation,
                toStation = route.toStation,
                showRouteResult = false,
                routeError = ""
            )
        }
        // Auto-find route
        onFindRoute()
    }

    // ── Logout ──────────────────────────────────────────────────────────────

    fun onLogout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            sessionDataStore.clearSession()
            onLoggedOut()
        }
    }

    // ── Factory ─────────────────────────────────────────────────────────────

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val savedRoutesStore = SavedRoutesDataStore(context)
            val sessionStore = SessionDataStore(context)
            val homeRepo = HomeRepository(savedRoutesDataStore = savedRoutesStore)
            val findRouteUseCase = FindRouteUseCase(homeRepo)
            return HomeViewModel(homeRepo, findRouteUseCase, sessionStore) as T
        }
    }
}

