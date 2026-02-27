package com.example.metro.presentation.explore

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metro.R
import com.example.metro.data.model.PatnaPlace
import com.example.metro.data.model.PlaceCategory
import com.example.metro.ui.theme.*

// ─── Explore Screen ───────────────────────────────────────────────────────────

@Composable
fun ExploreScreen(vm: ExploreViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Parchment)
    ) {
        // ── Header ────────────────────────────────────────────────────────
        ExploreHeader()

        // ── Title ─────────────────────────────────────────────────────────
        Text(
            "Discover the Best Places\nto Visit in Patna",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDark,
                lineHeight = 34.sp
            ),
            textAlign = TextAlign.Center
        )

        // ── Category filter chips ─────────────────────────────────────────
        CategoryChips(
            selectedCategory = state.selectedCategory,
            onCategoryChange = vm::onCategoryChange
        )

        Spacer(Modifier.height(12.dp))

        // ── Places List ───────────────────────────────────────────────────
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(state.filteredPlaces, key = { it.id }) { place ->
                PlaceItem(
                    place = place,
                    isExpanded = state.expandedPlaceId == place.id,
                    onClick = { vm.onPlaceTap(place.id) }
                )
            }
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun ExploreHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.borders),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Parchment.copy(alpha = 0.75f))
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
                    painter = painterResource(R.drawable.mento),
                    contentDescription = "Logo",
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Explore Patna",
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

// ── Category Filter Chips ─────────────────────────────────────────────────────

@Composable
private fun CategoryChips(
    selectedCategory: PlaceCategory?,
    onCategoryChange: (PlaceCategory?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "All" chip
        FilterChip(
            label = "All",
            isActive = selectedCategory == null,
            color = VermilionRed,
            onClick = { onCategoryChange(null) }
        )
        PlaceCategory.entries.forEach { cat ->
            val chipColor = when (cat) {
                PlaceCategory.HERITAGE  -> Color(0xFFB8860B)
                PlaceCategory.RELIGIOUS -> Color(0xFF8E24AA)
                PlaceCategory.NATURE    -> Color(0xFF2E7D32)
                PlaceCategory.MUSEUM    -> IndigoBlue
                PlaceCategory.LANDMARK  -> VermilionRed
            }
            FilterChip(
                label = cat.label,
                isActive = selectedCategory == cat,
                color = chipColor,
                onClick = { onCategoryChange(cat) }
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isActive: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isActive) color else SurfaceWhite,
        shadowElevation = if (isActive) 0.dp else 1.dp
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge.copy(
                color = if (isActive) Color.White else TextMedium,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
            )
        )
    }
}

// ── Place Item (Expandable Accordion) ─────────────────────────────────────────

@Composable
private fun PlaceItem(
    place: PatnaPlace,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            // ── Header row (always visible) ───────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    place.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.width(12.dp))
                Icon(
                    if (isExpanded) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = TextLight,
                    modifier = Modifier.size(24.dp)
                )
            }

            // ── Expanded detail content ───────────────────────────────────
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                PlaceDetail(place)
            }

            // Divider
            HorizontalDivider(color = DividerColor, thickness = 0.5.dp)
        }
    }
}

// ── Place Detail (shown when expanded) ────────────────────────────────────────

@Composable
private fun PlaceDetail(place: PatnaPlace) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
    ) {
        // ── Image placeholder with emoji + gradient ───────────────────────
        val gradientColors = when (place.category) {
            PlaceCategory.HERITAGE  -> listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))
            PlaceCategory.RELIGIOUS -> listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))
            PlaceCategory.NATURE    -> listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
            PlaceCategory.MUSEUM    -> listOf(Color(0xFFE8EAF6), Color(0xFFC5CAE9))
            PlaceCategory.LANDMARK  -> listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Brush.verticalGradient(gradientColors)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(place.emoji, fontSize = 48.sp)
                Spacer(Modifier.height(4.dp))
                // Category badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.8f)
                ) {
                    Text(
                        place.category.label,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextMedium
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // ── Hindi name ────────────────────────────────────────���───────────
        Text(
            place.nameHindi,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextMedium
            )
        )

        Spacer(Modifier.height(10.dp))

        // ── Description ───────────────────────────────────────────────────
        Text(
            place.description,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextDark,
                lineHeight = 22.sp
            )
        )

        Spacer(Modifier.height(14.dp))

        // ── Highlights chips ──────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            place.highlights.forEach { highlight ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ParchmentDark
                ) {
                    Text(
                        highlight,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextMedium,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // ── Info rows ─────────────────────────────────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = ParchmentDark),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Nearest station
                InfoRow(
                    icon = { Icon(Icons.Filled.Train, null, tint = VermilionRed, modifier = Modifier.size(16.dp)) },
                    label = "Nearest Station",
                    value = place.nearestStation
                )
                Spacer(Modifier.height(4.dp))
                InfoRow(
                    icon = { Icon(Icons.Outlined.LocationOn, null, tint = IndigoBlue, modifier = Modifier.size(16.dp)) },
                    label = "Distance",
                    value = place.distance
                )
                Spacer(Modifier.height(4.dp))
                InfoRow(
                    icon = { Icon(Icons.Outlined.AccessTime, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp)) },
                    label = "Timings",
                    value = place.timings
                )
                Spacer(Modifier.height(4.dp))
                InfoRow(
                    icon = { Text("🎫", fontSize = 14.sp) },
                    label = "Entry Fee",
                    value = place.entryFee
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: @Composable () -> Unit,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        icon()
        Spacer(Modifier.width(8.dp))
        Text(
            "$label: ",
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextLight,
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            value,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextDark,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

