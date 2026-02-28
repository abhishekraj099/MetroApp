package com.example.metro.presentation.home

import android.content.Context
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

data class HomeUiState(
    // Route planner
    val fromStation: String = "",
    val toStation: String = "",
    val stationNames: List<String> = emptyList(),
    val fromSuggestions: List<String> = emptyList(),
    val toSuggestions: List<String> = emptyList(),
    val showFromSuggestions: Boolean = false,
    val showToSuggestions: Boolean = false,

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
    val loggedInEmail: String = ""
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
        _uiState.update { it.copy(stationNames = names) }
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

    // ── From station input ──────────────────────────────────────────────────

    fun onFromStationChange(value: String) {
        val suggestions = if (value.isNotBlank()) {
            _uiState.value.stationNames.filter {
                it.lowercase().contains(value.lowercase())
            }
        } else emptyList()

        _uiState.update {
            it.copy(
                fromStation = value,
                fromSuggestions = suggestions,
                showFromSuggestions = suggestions.isNotEmpty() && value.isNotBlank(),
                showRouteResult = false,
                routeError = ""
            )
        }
    }

    fun onFromStationSelected(name: String) {
        _uiState.update {
            it.copy(
                fromStation = name,
                showFromSuggestions = false,
                fromSuggestions = emptyList()
            )
        }
    }

    // ── To station input ────────────────────────────────────────────────────

    fun onToStationChange(value: String) {
        val suggestions = if (value.isNotBlank()) {
            _uiState.value.stationNames.filter {
                it.lowercase().contains(value.lowercase())
            }
        } else emptyList()

        _uiState.update {
            it.copy(
                toStation = value,
                toSuggestions = suggestions,
                showToSuggestions = suggestions.isNotEmpty() && value.isNotBlank(),
                showRouteResult = false,
                routeError = ""
            )
        }
    }

    fun onToStationSelected(name: String) {
        _uiState.update {
            it.copy(
                toStation = name,
                showToSuggestions = false,
                toSuggestions = emptyList()
            )
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

