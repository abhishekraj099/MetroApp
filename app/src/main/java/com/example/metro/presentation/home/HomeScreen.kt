package com.example.metro.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
        NavItem("Home",     Icons.Filled.Home,        Icons.Outlined.Home),
        NavItem("Map",      Icons.Filled.Map,         Icons.Outlined.Map),
        NavItem("Stations", Icons.Filled.Train,       Icons.Outlined.Train),
        NavItem("Alerts",   Icons.Filled.Notifications, Icons.Outlined.Notifications),
        NavItem("Style",    Icons.Filled.Info,        Icons.Outlined.Info)
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

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
fun MetroHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
    ) {
        // Spacer for status bar, then border strip
        Spacer(Modifier.statusBarsPadding())

        // Decorative border image — full width, sits at the very top
        Image(
            painter = painterResource(R.drawable.borders),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            contentScale = ContentScale.FillBounds
        )

        // Top bar content row — sits cleanly below the border
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo + Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(VermilionRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Train,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        "Patna Metro",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        "UNOFFICIAL GUIDE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = VermilionRed,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            fontSize = 9.sp
                        )
                    )
                }
            }

            // Settings icon
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(ParchmentDark)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = IndigoBlue,
                    modifier = Modifier.size(20.dp)
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

            // Station selector
            Box {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // From Station
                    StationField(
                        label = "From Station",
                        value = fromStation,
                        dotColor = IndigoBlue,
                        onValueChange = onFromChange
                    )
                    // To Station
                    StationField(
                        label = "To Station",
                        value = toStation,
                        dotColor = VermilionRed,
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
    dotColor: Color,
    placeholder: String = "",
    onValueChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = ParchmentDark,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dot indicator
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Spacer(Modifier.width(12.dp))
            Column {
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
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp))
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
            // Green lightning icon
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
