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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
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
    val iconTint: Color
)

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

// ── Main Screen ───────────────────────────────────────────────────────────────

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory(context)
    )
    val uiState by viewModel.uiState.collectAsState()

    val quickActions = listOf(
        QuickAction("Fare",     Icons.Outlined.ConfirmationNumber, Color(0xFFFFF3E0), Color(0xFFE65142)),
        QuickAction("Timings",  Icons.Outlined.Schedule,           Color(0xFFE8EAF6), IndigoBlue),
        QuickAction("Nearest",  Icons.Outlined.NearMe,             Color(0xFFE8F5E9), Color(0xFF2E7D32)),
        QuickAction("Access",   Icons.Outlined.Accessible,         Color(0xFFEDE7F6), Color(0xFF6A1B9A))
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
                    onFromChange = viewModel::onFromStationChange,
                    onFromSelected = viewModel::onFromStationSelected,
                    onToChange = viewModel::onToStationChange,
                    onToSelected = viewModel::onToStationSelected,
                    onSwap = viewModel::onSwapStations,
                    onFindRoute = viewModel::onFindRoute,
                    onSaveRoute = viewModel::onSaveCurrentRoute,
                    onDismissRoute = viewModel::dismissRouteResult,
                    onLoadSavedRoute = viewModel::onLoadSavedRoute,
                    onRemoveSavedRoute = viewModel::onRemoveSavedRoute
                )
                1 -> MapScreen()
                2 -> StationsScreen()
                3 -> AlertsScreen()
                4 -> ExploreScreen()
                else -> HomeContent(
                    uiState = uiState,
                    quickActions = quickActions,
                    onFromChange = viewModel::onFromStationChange,
                    onFromSelected = viewModel::onFromStationSelected,
                    onToChange = viewModel::onToStationChange,
                    onToSelected = viewModel::onToStationSelected,
                    onSwap = viewModel::onSwapStations,
                    onFindRoute = viewModel::onFindRoute,
                    onSaveRoute = viewModel::onSaveCurrentRoute,
                    onDismissRoute = viewModel::dismissRouteResult,
                    onLoadSavedRoute = viewModel::onLoadSavedRoute,
                    onRemoveSavedRoute = viewModel::onRemoveSavedRoute
                )
            }
        }
    }
}

// ── Home Tab Content ──────────────────────────────────────────────────────────

@Composable
fun HomeContent(
    uiState: HomeUiState,
    quickActions: List<QuickAction>,
    onFromChange: (String) -> Unit,
    onFromSelected: (String) -> Unit,
    onToChange: (String) -> Unit,
    onToSelected: (String) -> Unit,
    onSwap: () -> Unit,
    onFindRoute: () -> Unit,
    onSaveRoute: () -> Unit,
    onDismissRoute: () -> Unit,
    onLoadSavedRoute: (SavedRoute) -> Unit,
    onRemoveSavedRoute: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MetroHeader()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PlanYourRideCard(
                fromStation = uiState.fromStation,
                toStation = uiState.toStation,
                fromSuggestions = uiState.fromSuggestions,
                toSuggestions = uiState.toSuggestions,
                showFromSuggestions = uiState.showFromSuggestions,
                showToSuggestions = uiState.showToSuggestions,
                onFromChange = onFromChange,
                onFromSelected = onFromSelected,
                onToChange = onToChange,
                onToSelected = onToSelected,
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

            QuickActionsRow(quickActions)
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
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
fun MetroHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        // Border image fills entire header as background
        Image(
            painter = painterResource(R.drawable.borders),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Semi-transparent overlay so text is readable
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Parchment.copy(alpha = 0.75f))
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
                    painter = painterResource(R.drawable.mento),
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
                    .clickable { },
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
    fromSuggestions: List<String>,
    toSuggestions: List<String>,
    showFromSuggestions: Boolean,
    showToSuggestions: Boolean,
    onFromChange: (String) -> Unit,
    onFromSelected: (String) -> Unit,
    onToChange: (String) -> Unit,
    onToSelected: (String) -> Unit,
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

            // Station selector with vertical connecting line + dots
            Row(modifier = Modifier.fillMaxWidth()) {
                // Left column: dots + vertical dashed line
                Column(
                    modifier = Modifier
                        .padding(top = 18.dp)
                        .width(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // From dot (blue)
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                            .drawBehind {
                                drawCircle(
                                    color = IndigoBlue,
                                    radius = size.minDimension / 2,
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                                )
                            }
                    )
                    // Dashed vertical line
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(52.dp)
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
                    // To dot (red)
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                            .drawBehind {
                                drawCircle(
                                    color = VermilionRed,
                                    radius = size.minDimension / 2,
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                                )
                            }
                    )
                }

                Spacer(Modifier.width(8.dp))

                // Station fields + swap button
                Box(modifier = Modifier.weight(1f)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        StationFieldWithSuggestions(
                            label = "From Station",
                            value = fromStation,
                            placeholder = "Select origin",
                            suggestions = fromSuggestions,
                            showSuggestions = showFromSuggestions,
                            onValueChange = onFromChange,
                            onSuggestionSelected = onFromSelected
                        )
                        StationFieldWithSuggestions(
                            label = "To Station",
                            value = toStation,
                            placeholder = "Select destination",
                            suggestions = toSuggestions,
                            showSuggestions = showToSuggestions,
                            onValueChange = onToChange,
                            onSuggestionSelected = onToSelected
                        )
                    }

                    // Swap button
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp)
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(ParchmentDark)
                            .clickable { onSwap() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.SwapVert,
                            contentDescription = "Swap",
                            tint = IndigoBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
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

// ── Station Field with Autocomplete Suggestions ─────────────────────────────

@Composable
fun StationFieldWithSuggestions(
    label: String,
    value: String,
    placeholder: String = "",
    suggestions: List<String>,
    showSuggestions: Boolean,
    onValueChange: (String) -> Unit,
    onSuggestionSelected: (String) -> Unit
) {
    Column {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = ParchmentDark
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextLight, fontSize = 11.sp
                    )
                )
                Spacer(Modifier.height(2.dp))
                // Editable text field
                androidx.compose.foundation.text.BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = MaterialTheme.typography.titleSmall.copy(
                        color = TextDark,
                        fontWeight = FontWeight.SemiBold
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box {
                            if (value.isEmpty()) {
                                Text(
                                    placeholder,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = TextLight,
                                        fontWeight = FontWeight.Normal
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        // Dropdown suggestions
        AnimatedVisibility(
            visible = showSuggestions,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column {
                    suggestions.take(5).forEach { name ->
                        Text(
                            name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSuggestionSelected(name) }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextDark)
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
fun QuickActionsRow(actions: List<QuickAction>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        actions.forEach { action ->
            QuickActionItem(action, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun QuickActionItem(action: QuickAction, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.clickable { },
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
