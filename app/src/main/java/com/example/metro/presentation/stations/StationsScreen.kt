package com.example.metro.presentation.stations

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metro.R
import com.example.metro.data.model.Station
import com.example.metro.ui.theme.*

// ─── Stations Screen ──────────────────────────────────────────────────────────

@Composable
fun StationsScreen(vm: StationsViewModel = viewModel(), onNavigateToSettings: () -> Unit = {}) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Parchment)
    ) {
        // ── Header ────────────────────────────────────────────────────────
        StationsHeader(onNavigateToSettings = onNavigateToSettings)

        // ── Content ───────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // ── Search Bar ────────────────────────────────────────────────
            SearchBar(
                query = state.searchQuery,
                onQueryChange = vm::onSearchQueryChange
            )

            Spacer(Modifier.height(16.dp))

            // ── Filter Chips ──────────────────────────────────────────────
            FilterChips(
                activeFilter = state.activeFilter,
                onFilterChange = vm::onFilterChange
            )

            Spacer(Modifier.height(16.dp))

            // ── Station List ──────────────────────────────────────────────
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(state.filteredStations, key = { it.id }) { station ->
                    StationCard(
                        station = station,
                        onClick = { vm.onStationSelected(station) }
                    )
                }
            }
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun StationsHeader(onNavigateToSettings: () -> Unit) {
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
                        "Stations",
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

// ── Search Bar ────────────────────────────────────────────────────────────────

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                "Search stations...",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextLight)
            )
        },
        leadingIcon = {
            Icon(
                Icons.Outlined.Search,
                contentDescription = "Search",
                tint = TextLight
            )
        },
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextDark,
            unfocusedTextColor = TextDark,
            focusedContainerColor = SurfaceWhite,
            unfocusedContainerColor = SurfaceWhite,
            focusedBorderColor = VermilionRed,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = VermilionRed,
            focusedLeadingIconColor = VermilionRed,
            unfocusedLeadingIconColor = TextLight,
            focusedPlaceholderColor = TextLight,
            unfocusedPlaceholderColor = TextLight
        )
    )
}

// ── Filter Chips ──────────────────────────────────────────────────────────────

@Composable
private fun FilterChips(activeFilter: LineFilter, onFilterChange: (LineFilter) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        LineFilter.entries.forEach { filter ->
            val isActive = filter == activeFilter
            Surface(
                modifier = Modifier.clickable { onFilterChange(filter) },
                shape = RoundedCornerShape(20.dp),
                color = if (isActive) {
                    when (filter) {
                        LineFilter.ALL  -> VermilionRed
                        LineFilter.RED  -> VermilionRed
                        LineFilter.BLUE -> IndigoBlue
                    }
                } else SurfaceWhite,
                tonalElevation = if (isActive) 0.dp else 1.dp,
                shadowElevation = if (isActive) 0.dp else 1.dp
            ) {
                Text(
                    filter.label,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = if (isActive) Color.White else TextMedium,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                    )
                )
            }
        }
    }
}

// ── Station Card ──────────────────────────────────────────────────────────────

@Composable
private fun StationCard(station: Station, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Train icon ────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ParchmentDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Train,
                    contentDescription = null,
                    tint = TextMedium,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            // ── Station info ──────────────────────────────────────────────
            Column(modifier = Modifier.weight(1f)) {
                // Interchange dot + name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (station.isInterchange) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Turmeric)
                        )
                        Spacer(Modifier.width(6.dp))
                    }
                    Text(
                        station.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    )
                }

                // Hindi name
                if (station.nameHindi.isNotEmpty()) {
                    Text(
                        station.nameHindi,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMedium
                        )
                    )
                }

                Spacer(Modifier.height(6.dp))

                // Line tags + facility icons row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    station.lines.forEach { line ->
                        LineTag(line)
                    }
                    // Accessibility icon if wheelchair
                    if ("Wheelchair" in station.facilities || "Lift" in station.facilities) {
                        Text(
                            "♿",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }
            }

            // ── ETA badge ─────────────────────────────────────────────────
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFE8F5E9),
                tonalElevation = 0.dp
            ) {
                Text(
                    "${station.nextTrainMin} min",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

// ── Line Tag Chip ─────────────────────────────────────────────────────────────

@Composable
private fun LineTag(line: String) {
    val color = when (line) {
        "RED"  -> VermilionRed
        "BLUE" -> IndigoBlue
        else   -> TextMedium
    }
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color
    ) {
        Text(
            line,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp
            )
        )
    }
}

