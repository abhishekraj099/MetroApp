package com.example.metro.presentation.home

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metro.R
import com.example.metro.ui.theme.*

// ── Data models ───────────────────────────────────────────────────────────────

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

data class SavedRoute(
    val lineName: String,
    val lineColor: Color,
    val from: String,
    val to: String
)

// ── Main Screen ───────────────────────────────────────────────────────────────

@Composable
fun HomeScreen() {
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
        NavItem("Style",    Icons.Filled.Info,           Icons.Outlined.Info)
    )

    val savedRoutes = listOf(
        SavedRoute("Red Line",  VermilionRed, "Patna Jn",    "Frazer Road"),
        SavedRoute("Blue Line", IndigoBlue,   "Saguna More", "Danapur")
    )

    var selectedNav by remember { mutableIntStateOf(0) }
    var fromStation by remember { mutableStateOf("Patna Junction") }
    var toStation   by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Parchment,
        bottomBar = {
            MetroBottomNav(navItems, selectedNav) { selectedNav = it }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Header with border image ───────────────────────────────────
            MetroHeader()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── Plan your ride card ───────────────────────────────────
                PlanYourRideCard(
                    fromStation = fromStation,
                    toStation   = toStation,
                    onFromChange = { fromStation = it },
                    onToChange   = { toStation = it },
                    onSwap = {
                        val temp = fromStation
                        fromStation = toStation
                        toStation = temp
                    }
                )

                // ── Quick Actions ─────────────────────────────────────────
                QuickActionsRow(quickActions)

                // ── Service Status ────────────────────────────────────────
                ServiceStatusCard()

                // ── Saved Routes ──────────────────────────────────────────
                SavedRoutesSection(savedRoutes)

                Spacer(Modifier.height(8.dp))
            }
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
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
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
    onFromChange: (String) -> Unit,
    onToChange: (String) -> Unit,
    onSwap: () -> Unit
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
                        StationField(
                            label = "From Station",
                            value = fromStation,
                            onValueChange = onFromChange
                        )
                        StationField(
                            label = "To Station",
                            value = toStation,
                            placeholder = "Select destination",
                            onValueChange = onToChange
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

            Spacer(Modifier.height(16.dp))

            // Find Route button
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VermilionRed)
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

@Composable
fun StationField(
    label: String,
    value: String,
    placeholder: String = "",
    onValueChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = ParchmentDark,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextLight, fontSize = 11.sp
                )
            )
            Text(
                value.ifEmpty { placeholder },
                style = MaterialTheme.typography.titleSmall.copy(
                    color = if (value.isEmpty()) TextLight else TextDark,
                    fontWeight = if (value.isEmpty()) FontWeight.Normal else FontWeight.SemiBold
                )
            )
        }
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
fun ServiceStatusCard() {
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
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Bolt,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    "Normal Service",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextDark, fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "All lines running smoothly today.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMedium)
                )
            }
        }
    }
}

// ── Saved Routes Section ──────────────────────────────────────────────────────

@Composable
fun SavedRoutesSection(routes: List<SavedRoute>) {
    Column {
        Text(
            "Saved Routes",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = TextDark,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            routes.forEach { route ->
                SavedRouteCard(route)
            }
        }
    }
}

@Composable
fun SavedRouteCard(route: SavedRoute) {
    Card(
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Line name with colored dot
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(route.lineColor)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    route.lineName,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TextMedium
                    )
                )
            }
            Spacer(Modifier.height(8.dp))
            // From ⇄ To
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    route.from,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextDark,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "⇄",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextLight
                    )
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    route.to,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextDark,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
