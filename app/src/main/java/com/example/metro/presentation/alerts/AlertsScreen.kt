package com.example.metro.presentation.alerts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metro.R
import com.example.metro.data.model.AlertType
import com.example.metro.data.model.MetroAlert
import com.example.metro.ui.theme.*

// ─── Alerts Screen ────────────────────────────────────────────────────────────

@Composable
fun AlertsScreen(vm: AlertsViewModel = viewModel(), onNavigateToSettings: () -> Unit = {}) {
    val state by vm.uiState.collectAsState()

    val displayAlerts = when (state.activeTab) {
        AlertTab.SERVICE_STATUS -> state.serviceAlerts
        AlertTab.PLANNED_WORKS  -> state.plannedAlerts
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Parchment)
    ) {
        // ── Header ────────────────────────────────────────────────────────
        AlertsHeader(onNavigateToSettings = onNavigateToSettings)

        // ── Tabs ──────────────────────────────────────────────────────────
        AlertsTabs(
            activeTab = state.activeTab,
            onTabChange = vm::onTabChange
        )

        // ── Alert List ────────────────────────────────────────────────────
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            items(displayAlerts, key = { it.id }) { alert ->
                AlertCard(
                    alert = alert,
                    isExpanded = state.expandedAlertId == alert.id,
                    onClick = { vm.onAlertTap(alert.id) }
                )
            }

            // ── All Clear footer ──────────────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                AllClearCard()
            }
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun AlertsHeader(onNavigateToSettings: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.newborder),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Parchment.copy(alpha = 0.85f))
        )
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
                    contentDescription = "Logo",
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Alerts & Info",
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
                    .clickable { onNavigateToSettings() },
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

// ── Tabs ──────────────────────────────────────────────────────────────────────

@Composable
private fun AlertsTabs(activeTab: AlertTab, onTabChange: (AlertTab) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        color = SurfaceWhite,
        shadowElevation = 2.dp
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            AlertTab.entries.forEach { tab ->
                val isActive = tab == activeTab
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabChange(tab) },
                    shape = RoundedCornerShape(24.dp),
                    color = if (isActive) TextDark else Color.Transparent
                ) {
                    Text(
                        tab.label,
                        modifier = Modifier.padding(vertical = 12.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = if (isActive) Color.White else TextMedium,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

// ── Alert Card ────────────────────────────────────────────────────────────────

@Composable
private fun AlertCard(alert: MetroAlert, isExpanded: Boolean, onClick: () -> Unit) {
    val accentColor = when (alert.type) {
        AlertType.INFO         -> IndigoBlue
        AlertType.ANNOUNCEMENT -> Turmeric
        AlertType.RULE         -> IndigoBlue
        AlertType.HELPLINE     -> Color(0xFF2E7D32)
        AlertType.TIP          -> VermilionRed
    }

    val iconEmoji = when (alert.type) {
        AlertType.INFO         -> "ℹ️"
        AlertType.ANNOUNCEMENT -> "📢"
        AlertType.RULE         -> "📋"
        AlertType.HELPLINE     -> "📞"
        AlertType.TIP          -> "💡"
    }

    val bgTint = accentColor.copy(alpha = 0.06f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        // Left accent bar
        Row {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(accentColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Icon circle
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(bgTint),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(iconEmoji, fontSize = 18.sp)
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            alert.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        )
                        Spacer(Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⏱", fontSize = 11.sp)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                alert.subtitle,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextLight
                                )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Description — always visible for first 2 lines,
                // full text when expanded
                Text(
                    alert.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextMedium,
                        lineHeight = 22.sp
                    ),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 3
                )

                // Expand/collapse indicator
                AnimatedVisibility(
                    visible = !isExpanded,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        "Tap to read more",
                        modifier = Modifier.padding(top = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = accentColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

// ── All Clear Card ────────────────────────────────────────────────────────────

@Composable
private fun AllClearCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Green check circle
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "All Clear",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "No other service disruptions reported across the\nnetwork.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextMedium
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

