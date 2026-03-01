package com.example.metro.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Accessible
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metro.R
import com.example.metro.data.model.RouteResult
import com.example.metro.data.model.SavedRoute
import com.example.metro.data.model.ServiceLevel
import com.example.metro.presentation.alerts.AlertsScreen
import com.example.metro.presentation.explore.ExploreScreen
import com.example.metro.presentation.map.MapScreen
import com.example.metro.presentation.stations.StationsScreen
import com.example.metro.ui.theme.*

// ── Data models for UI ────────────────────────────────────────────────────────

data class QuickAction(
    val label: String,
    val icon: ImageVector,
    val bgColor: Color,
    val iconTint: Color,
    val type: QuickActionType? = null
)

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

// ── Main Screen ───────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(onNavigateToSettings: () -> Unit = {}) {
    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory(context)
    )
    val uiState by viewModel.uiState.collectAsState()

    val quickActions = listOf(
        QuickAction("Fare",     Icons.Outlined.ConfirmationNumber, Color(0xFFFFF3E0), Color(0xFFE65142), QuickActionType.FARE),
        QuickAction("Timings",  Icons.Outlined.Schedule,           Color(0xFFE8EAF6), IndigoBlue, QuickActionType.TIMINGS),
        QuickAction("Nearest",  Icons.Outlined.NearMe,             Color(0xFFE8F5E9), Color(0xFF2E7D32), QuickActionType.NEAREST),
        QuickAction("Access",   Icons.AutoMirrored.Outlined.Accessible,         Color(0xFFEDE7F6), Color(0xFF6A1B9A), QuickActionType.ACCESSIBILITY)
    )

    val navItems = listOf(
        NavItem("Home",     Icons.Filled.Home,           Icons.Outlined.Home),
        NavItem("Map",      Icons.Filled.Map,            Icons.Outlined.Map),
        NavItem("Stations", Icons.Filled.Train,          Icons.Outlined.Train),
        NavItem("Alerts",   Icons.Filled.Notifications,  Icons.Outlined.Notifications),
        NavItem("Explore",  Icons.Filled.Explore,        Icons.Outlined.Explore)
    )

    var selectedNav by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Parchment,
        bottomBar = {
            MetroBottomNav(navItems, selectedNav) { selectedNav = it }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedNav) {
                0 -> HomeContent(
                    uiState = uiState,
                    quickActions = quickActions,
                    onFromFieldClick = { viewModel.openStationPicker(StationPickerTarget.FROM) },
                    onToFieldClick = { viewModel.openStationPicker(StationPickerTarget.TO) },
                    onSwap = viewModel::onSwapStations,
                    onFindRoute = viewModel::onFindRoute,
                    onSaveRoute = viewModel::onSaveCurrentRoute,
                    onDismissRoute = viewModel::dismissRouteResult,
                    onLoadSavedRoute = viewModel::onLoadSavedRoute,
                    onRemoveSavedRoute = viewModel::onRemoveSavedRoute,
                    onPickerQueryChange = viewModel::onPickerQueryChange,
                    onStationSelected = viewModel::onStationSelected,
                    onDismissPicker = viewModel::dismissStationPicker,
                    onQuickActionClick = viewModel::onQuickActionOpen,
                    onDismissQuickAction = viewModel::dismissQuickAction,
                    onFareFromClick = { viewModel.openFareStationPicker(StationPickerTarget.FARE_FROM) },
                    onFareToClick = { viewModel.openFareStationPicker(StationPickerTarget.FARE_TO) },
                    onSettingsClick = onNavigateToSettings
                )
                1 -> MapScreen(onNavigateToSettings = onNavigateToSettings)
                2 -> StationsScreen(onNavigateToSettings = onNavigateToSettings)
                3 -> AlertsScreen(onNavigateToSettings = onNavigateToSettings)
                4 -> ExploreScreen(onNavigateToSettings = onNavigateToSettings)
                else -> HomeContent(
                    uiState = uiState,
                    quickActions = quickActions,
                    onFromFieldClick = { viewModel.openStationPicker(StationPickerTarget.FROM) },
                    onToFieldClick = { viewModel.openStationPicker(StationPickerTarget.TO) },
                    onSwap = viewModel::onSwapStations,
                    onFindRoute = viewModel::onFindRoute,
                    onSaveRoute = viewModel::onSaveCurrentRoute,
                    onDismissRoute = viewModel::dismissRouteResult,
                    onLoadSavedRoute = viewModel::onLoadSavedRoute,
                    onRemoveSavedRoute = viewModel::onRemoveSavedRoute,
                    onPickerQueryChange = viewModel::onPickerQueryChange,
                    onStationSelected = viewModel::onStationSelected,
                    onDismissPicker = viewModel::dismissStationPicker,
                    onQuickActionClick = viewModel::onQuickActionOpen,
                    onDismissQuickAction = viewModel::dismissQuickAction,
                    onFareFromClick = { viewModel.openFareStationPicker(StationPickerTarget.FARE_FROM) },
                    onFareToClick = { viewModel.openFareStationPicker(StationPickerTarget.FARE_TO) },
                    onSettingsClick = onNavigateToSettings
                )
            }
        }
    }
}

// ── Home Tab Content ──────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    quickActions: List<QuickAction>,
    onFromFieldClick: () -> Unit,
    onToFieldClick: () -> Unit,
    onSwap: () -> Unit,
    onFindRoute: () -> Unit,
    onSaveRoute: () -> Unit,
    onDismissRoute: () -> Unit,
    onLoadSavedRoute: (SavedRoute) -> Unit,
    onRemoveSavedRoute: (String) -> Unit,
    onPickerQueryChange: (String) -> Unit,
    onStationSelected: (String) -> Unit,
    onDismissPicker: () -> Unit,
    onQuickActionClick: (QuickActionType) -> Unit,
    onDismissQuickAction: () -> Unit,
    onFareFromClick: () -> Unit,
    onFareToClick: () -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            MetroHeader(onSettingsClick = onSettingsClick)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PlanYourRideCard(
                    fromStation = uiState.fromStation,
                    toStation = uiState.toStation,
                    onFromClick = onFromFieldClick,
                    onToClick = onToFieldClick,
                    onSwap = onSwap,
                    onFindRoute = onFindRoute,
                    routeError = uiState.routeError
                )

                // Route Result Card
                AnimatedVisibility(
                    visible = uiState.showRouteResult && uiState.routeResult != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    uiState.routeResult?.let { result ->
                        RouteResultCard(
                            result = result,
                            onSave = onSaveRoute,
                            onDismiss = onDismissRoute
                        )
                    }
                }

                QuickActionsRow(quickActions, onQuickActionClick)
                ServiceStatusCard(
                    serviceLevel = uiState.serviceLevel,
                    message = uiState.serviceMessage
                )
                SavedRoutesSection(
                    routes = uiState.savedRoutes,
                    onRouteClick = onLoadSavedRoute,
                    onRouteRemove = onRemoveSavedRoute
                )
                Spacer(Modifier.height(8.dp))
            }
        }

        // Quick Action Overlays (rendered BEFORE station picker so picker appears on top)
        when (uiState.activeQuickAction) {
            QuickActionType.FARE -> FareCalculatorOverlay(
                fareFrom = uiState.fareFromStation,
                fareTo = uiState.fareToStation,
                fareResult = uiState.fareResult,
                fareStationCount = uiState.fareStationCount,
                onFromClick = onFareFromClick,
                onToClick = onFareToClick,
                onDismiss = onDismissQuickAction
            )
            QuickActionType.TIMINGS -> TimingsOverlay(onDismiss = onDismissQuickAction)
            QuickActionType.NEAREST -> NearestStationOverlay(onDismiss = onDismissQuickAction)
            QuickActionType.ACCESSIBILITY -> AccessibilityOverlay(onDismiss = onDismissQuickAction)
            null -> { /* no overlay */ }
        }

        // Station picker overlay (rendered LAST so it appears on top of everything)
        if (uiState.showStationPicker) {
            StationPickerBottomSheet(
                title = when (uiState.pickerTarget) {
                    StationPickerTarget.FROM, StationPickerTarget.FARE_FROM -> "Select Origin"
                    StationPickerTarget.TO, StationPickerTarget.FARE_TO -> "Select Destination"
                },
                query = uiState.pickerQuery,
                allStations = uiState.allStations,
                corridor1Stations = uiState.corridor1Stations,
                corridor2Stations = uiState.corridor2Stations,
                onQueryChange = onPickerQueryChange,
                onStationSelected = onStationSelected,
                onDismiss = onDismissPicker
            )
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
fun MetroHeader(onSettingsClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        // Border image fills entire header as background
        Image(
            painter = painterResource(R.drawable.newborder),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Semi-transparent overlay so text is readable
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Parchment.copy(alpha = 0.85f))
        )

        // Content row on top of border image
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.newlogo),
                    contentDescription = "Patna Metro Logo",
                    modifier = Modifier
                        .size(48.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Patna Metro",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            fontSize = 20.sp
                        )
                    )
                    Text(
                        "UNOFFICIAL GUIDE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = VermilionRed,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(ParchmentDark.copy(alpha = 0.8f))
                    .clickable { onSettingsClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = IndigoBlue,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

// ── Plan Your Ride Card ───────────────────────────────────────────────────────

@Composable
fun PlanYourRideCard(
    fromStation: String,
    toStation: String,
    onFromClick: () -> Unit,
    onToClick: () -> Unit,
    onSwap: () -> Unit,
    onFindRoute: () -> Unit,
    routeError: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Navigation,
                    contentDescription = null,
                    tint = VermilionRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Plan your ride",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = TextDark, fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(Modifier.height(16.dp))

            // Station row: [dots column] [fields column] [swap button]
            // NO overlapping boxes — each element is a sibling in the Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ── Left dots + dashed line column ──
                Column(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp)
                        .width(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .drawBehind {
                                drawCircle(
                                    color = IndigoBlue,
                                    radius = size.minDimension / 2,
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                                        width = 2.dp.toPx()
                                    )
                                )
                            }
                    )
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(56.dp)
                            .drawBehind {
                                drawLine(
                                    color = DividerColor,
                                    start = Offset(size.width / 2, 0f),
                                    end = Offset(size.width / 2, size.height),
                                    strokeWidth = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(6f, 6f), 0f
                                    )
                                )
                            }
                    )
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .drawBehind {
                                drawCircle(
                                    color = VermilionRed,
                                    radius = size.minDimension / 2,
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                                        width = 2.dp.toPx()
                                    )
                                )
                            }
                    )
                }

                Spacer(Modifier.width(10.dp))

                // ── Station fields column (takes all remaining space) ──
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StationClickableField(
                        label = "From Station",
                        value = fromStation,
                        placeholder = "Select origin",
                        onClick = onFromClick
                    )
                    StationClickableField(
                        label = "To Station",
                        value = toStation,
                        placeholder = "Select destination",
                        onClick = onToClick
                    )
                }

                Spacer(Modifier.width(8.dp))

                // ── Swap button (sibling, NOT overlapping) ──
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(ParchmentDark)
                        .clickable { onSwap() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.SwapVert,
                        contentDescription = "Swap",
                        tint = IndigoBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Error message
            if (routeError.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    routeError,
                    style = MaterialTheme.typography.bodySmall.copy(color = VermilionRed),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(16.dp))

            // Find Route button
            Button(
                onClick = onFindRoute,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VermilionRed),
                enabled = fromStation.isNotBlank() && toStation.isNotBlank()
            ) {
                Text(
                    "Find Route",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

// ── Clickable Station Field ──────────────────────────────────────────────────

@Composable
fun StationClickableField(
    label: String,
    value: String,
    placeholder: String = "",
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = ParchmentDark
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextLight, fontSize = 11.sp
                    )
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = value.ifEmpty { placeholder },
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = if (value.isEmpty()) TextLight else TextDark,
                        fontWeight = if (value.isEmpty()) FontWeight.Normal else FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Select",
                tint = TextLight,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ── Station Picker Bottom Sheet ─────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationPickerBottomSheet(
    title: String,
    query: String,
    allStations: List<com.example.metro.data.model.Station>,
    corridor1Stations: List<com.example.metro.data.model.Station>,
    corridor2Stations: List<com.example.metro.data.model.Station>,
    onQueryChange: (String) -> Unit,
    onStationSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Filter corridor stations based on search query
    val filteredRed = remember(query, corridor1Stations) {
        val q = query.lowercase().trim()
        if (q.isBlank()) corridor1Stations
        else corridor1Stations.filter {
            it.name.lowercase().contains(q) || it.nameHindi.contains(q)
        }
    }
    val filteredBlue = remember(query, corridor2Stations) {
        val q = query.lowercase().trim()
        if (q.isBlank()) corridor2Stations
        else corridor2Stations.filter {
            it.name.lowercase().contains(q) || it.nameHindi.contains(q)
        }
    }
    val totalCount = filteredRed.size + filteredBlue.size

    // Back button closes the picker
    androidx.activity.compose.BackHandler(onBack = onDismiss)

    // Full-screen overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Parchment)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar with close button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = TextDark
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = TextDark,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Search bar
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text("Search station...", color = TextLight)
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = null,
                        tint = TextLight
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "Clear",
                                tint = TextLight
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextDark,
                    unfocusedTextColor = TextDark,
                    focusedContainerColor = SurfaceWhite,
                    unfocusedContainerColor = SurfaceWhite,
                    focusedBorderColor = IndigoBlue,
                    unfocusedBorderColor = DividerColor,
                    cursorColor = IndigoBlue
                )
            )

            Spacer(Modifier.height(12.dp))

            // Station count
            Text(
                "$totalCount stations",
                style = MaterialTheme.typography.bodySmall.copy(color = TextLight),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(8.dp))

            // Scrollable station list
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                // Red Line — Corridor 1
                if (filteredRed.isNotEmpty()) {
                    item {
                        StationLineSectionHeader(
                            lineName = "Red Line — Corridor 1",
                            lineColor = VermilionRed,
                            stationCount = filteredRed.size
                        )
                    }
                    items(filteredRed.size) { index ->
                        val station = filteredRed[index]
                        StationPickerItem(
                            station = station,
                            lineColor = VermilionRed,
                            isFirst = index == 0,
                            isLast = index == filteredRed.lastIndex,
                            onClick = { onStationSelected(station.name) }
                        )
                    }
                    item { Spacer(Modifier.height(12.dp)) }
                }

                // Blue Line — Corridor 2
                if (filteredBlue.isNotEmpty()) {
                    item {
                        StationLineSectionHeader(
                            lineName = "Blue Line — Corridor 2",
                            lineColor = IndigoBlue,
                            stationCount = filteredBlue.size
                        )
                    }
                    items(filteredBlue.size) { index ->
                        val station = filteredBlue[index]
                        StationPickerItem(
                            station = station,
                            lineColor = IndigoBlue,
                            isFirst = index == 0,
                            isLast = index == filteredBlue.lastIndex,
                            onClick = { onStationSelected(station.name) }
                        )
                    }
                }

                // Empty state
                if (totalCount == 0) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Outlined.SearchOff,
                                contentDescription = null,
                                tint = TextLight,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "No stations found",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextLight)
                            )
                            Text(
                                "Try a different search term",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextLight)
                            )
                        }
                    }
                }

                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
fun StationLineSectionHeader(
    lineName: String,
    lineColor: Color,
    stationCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(lineColor)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            lineName,
            style = MaterialTheme.typography.labelLarge.copy(
                color = lineColor,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.weight(1f))
        Text(
            "$stationCount stations",
            style = MaterialTheme.typography.labelSmall.copy(color = TextLight)
        )
    }
}

@Composable
fun StationPickerItem(
    station: com.example.metro.data.model.Station,
    lineColor: Color,
    isFirst: Boolean,
    isLast: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Vertical line + station dot
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(52.dp),
                contentAlignment = Alignment.Center
            ) {
                // Vertical connecting line (top half)
                if (!isFirst) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .width(3.dp)
                            .fillMaxHeight(0.5f)
                            .background(lineColor.copy(alpha = 0.5f))
                    )
                }
                // Vertical connecting line (bottom half)
                if (!isLast) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .width(3.dp)
                            .fillMaxHeight(0.5f)
                            .background(lineColor.copy(alpha = 0.5f))
                    )
                }
                // Station dot — FILLED with line color
                Box(
                    modifier = Modifier
                        .size(if (station.isInterchange) 16.dp else 12.dp)
                        .clip(CircleShape)
                        .background(lineColor)
                        .then(
                            if (station.isInterchange) Modifier.drawBehind {
                                // Extra ring for interchange
                                drawCircle(
                                    color = lineColor,
                                    radius = size.minDimension / 2 + 3.dp.toPx(),
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                                )
                            } else Modifier
                        )
                )
            }

            Spacer(Modifier.width(12.dp))

            // Station info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    station.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = TextDark,
                        fontWeight = if (station.isInterchange) FontWeight.Bold else FontWeight.Medium
                    )
                )
                if (station.nameHindi.isNotEmpty()) {
                    Text(
                        station.nameHindi,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextLight, fontSize = 11.sp
                        )
                    )
                }
            }

            // Interchange badge
            if (station.isInterchange) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Turmeric.copy(alpha = 0.15f)
                ) {
                    Text(
                        "Interchange",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Turmeric,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Facility icons
            if (station.facilities.isNotEmpty()) {
                Spacer(Modifier.width(4.dp))
                Row {
                    if ("Parking" in station.facilities) {
                        Icon(
                            Icons.Outlined.LocalParking,
                            contentDescription = "Parking",
                            tint = TextLight,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    if ("Lift" in station.facilities) {
                        Icon(
                            Icons.Outlined.Elevator,
                            contentDescription = "Lift",
                            tint = TextLight,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

// ── Route Result Card ─────────────────────────────────────────────────────────

@Composable
fun RouteResultCard(
    result: RouteResult,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Route Found",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold, color = TextDark
                    )
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Filled.Close, contentDescription = "Close", tint = TextLight)
                }
            }

            Spacer(Modifier.height(12.dp))

            // Route summary row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RouteInfoChip(icon = Icons.Filled.Train, label = "${result.stationCount} Stations")
                RouteInfoChip(icon = Icons.Filled.Schedule, label = "~${result.estimatedTimeMin} min")
                RouteInfoChip(icon = Icons.Filled.CurrencyRupee, label = "₹${result.fare}")
            }

            Spacer(Modifier.height(12.dp))

            // Lines involved
            Row(verticalAlignment = Alignment.CenterVertically) {
                result.lines.forEach { line ->
                    val color = if (line == "RED") VermilionRed else IndigoBlue
                    val name = if (line == "RED") "Red Line" else "Blue Line"
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = color.copy(alpha = 0.12f),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                name,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold, color = color
                                )
                            )
                        }
                    }
                }
            }

            // Interchange info
            if (result.interchangeAt != null) {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.SwapHoriz,
                        contentDescription = null,
                        tint = Turmeric,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Change at ${result.interchangeAt}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMedium, fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(Modifier.height(12.dp))

            // Route path preview
            Text(
                "Route:",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = TextMedium, fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(Modifier.height(6.dp))
            Text(
                result.path.joinToString(" → ") { it.name },
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextDark, lineHeight = 18.sp
                )
            )

            Spacer(Modifier.height(14.dp))

            // Save route button
            OutlinedButton(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = IndigoBlue)
            ) {
                Icon(Icons.Filled.BookmarkAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Save Route", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun RouteInfoChip(icon: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = IndigoBlue, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextDark, fontWeight = FontWeight.Medium
            )
        )
    }
}

// ── Quick Actions Row ─────────────────────────────────────────────────────────

@Composable
fun QuickActionsRow(actions: List<QuickAction>, onActionClick: (QuickActionType) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        actions.forEach { action ->
            QuickActionItem(action, onActionClick, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun QuickActionItem(action: QuickAction, onActionClick: (QuickActionType) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.clickable { action.type?.let { onActionClick(it) } },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Circular icon background (matching the images)
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(action.bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                action.icon,
                contentDescription = action.label,
                tint = action.iconTint,
                modifier = Modifier.size(26.dp)
            )
        }
        Text(
            action.label,
            style = MaterialTheme.typography.labelMedium.copy(
                color = TextMedium, fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center
        )
    }
}

// ── Service Status Card ───────────────────────────────────────────────────────

@Composable
fun ServiceStatusCard(serviceLevel: ServiceLevel, message: String) {
    val (bgColor, iconColor, icon) = when (serviceLevel) {
        ServiceLevel.NORMAL      -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), Icons.Filled.Bolt)
        ServiceLevel.MINOR_DELAY -> Triple(Color(0xFFFFF3E0), Color(0xFFE65100), Icons.Filled.Warning)
        ServiceLevel.MAJOR_DELAY -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), Icons.Filled.Error)
        ServiceLevel.SUSPENDED   -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), Icons.Filled.Cancel)
    }
    val statusText = when (serviceLevel) {
        ServiceLevel.NORMAL      -> "Normal Service"
        ServiceLevel.MINOR_DELAY -> "Minor Delays"
        ServiceLevel.MAJOR_DELAY -> "Major Delays"
        ServiceLevel.SUSPENDED   -> "Service Suspended"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    statusText,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextDark, fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    message,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMedium)
                )
            }
        }
    }
}

// ── Saved Routes Section ──────────────────────────────────────────────────────

@Composable
fun SavedRoutesSection(
    routes: List<SavedRoute>,
    onRouteClick: (SavedRoute) -> Unit,
    onRouteRemove: (String) -> Unit
) {
    Column {
        Text(
            "Saved Routes",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = TextDark, fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(12.dp))

        if (routes.isEmpty()) {
            // Empty state
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Outlined.BookmarkBorder,
                        contentDescription = null,
                        tint = TextLight,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "No saved routes yet",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextLight)
                    )
                    Text(
                        "Find a route and save it for quick access",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextLight),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                routes.forEach { route ->
                    SavedRouteCard(
                        route = route,
                        onClick = { onRouteClick(route) },
                        onRemove = { onRouteRemove(route.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SavedRouteCard(
    route: SavedRoute,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    val lineColor = Color(route.lineColorHex)

    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header with line name and remove button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(lineColor)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        route.lineName,
                        style = MaterialTheme.typography.labelMedium.copy(color = TextMedium)
                    )
                }
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = "Remove",
                    tint = TextLight,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onRemove() }
                )
            }
            Spacer(Modifier.height(8.dp))
            // From ⇄ To
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    route.fromStation,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextDark, fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text("  ⇄  ", style = MaterialTheme.typography.titleSmall.copy(color = TextLight))
                Text(
                    route.toStation,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextDark, fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
            Spacer(Modifier.height(6.dp))
            // Fare + time
            Row {
                Text(
                    "₹${route.fare}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = IndigoBlue, fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "  •  ~${route.estimatedTime} min",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextLight)
                )
            }
        }
    }
}

// ── Bottom Navigation ─────────────────────────────────────────────────────────

@Composable
fun MetroBottomNav(
    items: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = SurfaceWhite,
        contentColor = IndigoBlue,
        tonalElevation = 8.dp
    ) {
        items.forEachIndexed { index, item ->
            val selected = index == selectedIndex
            NavigationBarItem(
                selected = selected,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        if (selected) item.selectedIcon else item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        item.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = VermilionRed,
                    selectedTextColor   = VermilionRed,
                    unselectedIconColor = TextLight,
                    unselectedTextColor = TextLight,
                    indicatorColor      = Color.Transparent
                )
            )
        }
    }
}
