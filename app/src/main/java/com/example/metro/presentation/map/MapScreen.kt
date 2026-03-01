package com.example.metro.presentation.map

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metro.R
import com.example.metro.data.model.MetroLine
import com.example.metro.data.model.Station
import com.example.metro.ui.theme.*

// ─── Map Screen ───────────────────────────────────────────────────────────────

@Composable
fun MapScreen(vm: MapViewModel = viewModel(), onNavigateToSettings: () -> Unit = {}) {
    val state by vm.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // ── Header ────────────────────────────────────────────────────────
        Column {
            MapHeader(onNavigateToSettings = onNavigateToSettings)

            // ── Map Canvas Area ───────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Parchment)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            vm.onPan(dragAmount.x, dragAmount.y)
                        }
                    }
            ) {
                // Canvas for drawing metro lines and stations
                MetroMapCanvas(
                    lines = state.lines,
                    selectedStation = state.selectedStation,
                    zoomLevel = state.zoomLevel,
                    offsetX = state.offsetX,
                    offsetY = state.offsetY,
                    showCorridor1 = state.showCorridor1,
                    showCorridor2 = state.showCorridor2,
                    onStationTap = vm::onStationTapped
                )

                // ── Info card overlay ─────────────────────────────────────
                Box(modifier = Modifier.align(Alignment.Center)) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = state.selectedStation == null,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        InteractiveMapCard()
                    }
                }

                // ── Station detail popup ──────────────────────────────────
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = state.selectedStation != null,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        state.selectedStation?.let { station ->
                            StationDetailCard(
                                station = station,
                                corridorColor = if (station.corridor == 1) IndigoBlue else VermilionRed,
                                onDismiss = vm::dismissStationDetail
                            )
                        }
                    }
                }

                // ── Zoom controls ─────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Reset view
                    ZoomButton(
                        icon = { Icon(Icons.Filled.MyLocation, contentDescription = "Reset", tint = TextDark, modifier = Modifier.size(20.dp)) },
                        onClick = vm::resetView
                    )
                    Spacer(Modifier.height(8.dp))
                    // Zoom in
                    ZoomButton(
                        icon = { Icon(Icons.Outlined.Add, contentDescription = "Zoom In", tint = TextDark, modifier = Modifier.size(20.dp)) },
                        onClick = vm::zoomIn
                    )
                    // Zoom out
                    ZoomButton(
                        icon = { Icon(Icons.Outlined.Remove, contentDescription = "Zoom Out", tint = TextDark, modifier = Modifier.size(20.dp)) },
                        onClick = vm::zoomOut
                    )
                }
            }
        }
    }
}

// ── Map Header (same style as HomeScreen) ─────────────────────────────────────

@Composable
fun MapHeader(onNavigateToSettings: () -> Unit = {}) {
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
                        "System Map",
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

// ── Metro Map Canvas ──────────────────────────────────────────────────────────

@Composable
fun MetroMapCanvas(
    lines: List<MetroLine>,
    selectedStation: Station?,
    zoomLevel: Float,
    offsetX: Float,
    offsetY: Float,
    showCorridor1: Boolean,
    showCorridor2: Boolean,
    onStationTap: (Station) -> Unit
) {

    // Compute bounds for mapping lat/lng → canvas coords
    val allStations = lines.flatMap { it.stations }
    if (allStations.isEmpty()) return

    val minLat = allStations.minOf { it.latitude }
    val maxLat = allStations.maxOf { it.latitude }
    val minLng = allStations.minOf { it.longitude }
    val maxLng = allStations.maxOf { it.longitude }

    // Track tap positions for station selection
    val stationPositions = remember { mutableStateMapOf<String, Offset>() }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(stationPositions.toMap()) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val pos = event.changes.firstOrNull()?.position ?: continue
                        // Find closest station within tap radius
                        val tapRadius = 40.dp.toPx()
                        val closest = stationPositions.entries.minByOrNull {
                            val dx = pos.x - it.value.x
                            val dy = pos.y - it.value.y
                            dx * dx + dy * dy
                        }
                        if (closest != null) {
                            val dx = pos.x - closest.value.x
                            val dy = pos.y - closest.value.y
                            if (dx * dx + dy * dy <= tapRadius * tapRadius) {
                                val station = allStations.find { it.id == closest.key }
                                if (station != null) {
                                    onStationTap(station)
                                }
                            }
                        }
                        event.changes.forEach { it.consume() }
                    }
                }
            }
    ) {
        val canvasW = size.width
        val canvasH = size.height

        val padH = canvasW * 0.12f
        val padV = canvasH * 0.1f
        val drawW = canvasW - 2 * padH
        val drawH = canvasH - 2 * padV

        // Map a station to canvas coordinates
        fun stationToOffset(station: Station): Offset {
            val latRange = (maxLat - minLat).coerceAtLeast(0.001)
            val lngRange = (maxLng - minLng).coerceAtLeast(0.001)

            val nx = ((station.longitude - minLng) / lngRange).toFloat()
            // Flip Y: higher lat → lower y
            val ny = ((maxLat - station.latitude) / latRange).toFloat()

            val baseX = padH + nx * drawW
            val baseY = padV + ny * drawH

            return Offset(
                x = (baseX - canvasW / 2) * zoomLevel + canvasW / 2 + offsetX,
                y = (baseY - canvasH / 2) * zoomLevel + canvasH / 2 + offsetY
            )
        }

        // Draw each corridor line
        lines.forEach { line ->
            val show = if (line.id == 1) showCorridor1 else showCorridor2
            if (!show) return@forEach

            val lineColor = line.color
            val offsets = line.stations.map { stationToOffset(it) }

            // Draw smooth corridor path
            if (offsets.size >= 2) {
                val path = Path().apply {
                    moveTo(offsets.first().x, offsets.first().y)
                    for (i in 1 until offsets.size) {
                        val prev = offsets[i - 1]
                        val curr = offsets[i]
                        val midX = (prev.x + curr.x) / 2
                        val midY = (prev.y + curr.y) / 2
                        quadraticTo(prev.x, prev.y, midX, midY)
                    }
                    lineTo(offsets.last().x, offsets.last().y)
                }
                // Outer glow
                drawPath(
                    path = path,
                    color = lineColor.copy(alpha = 0.15f),
                    style = Stroke(width = 16.dp.toPx() * zoomLevel, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
                // Main line
                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 5.dp.toPx() * zoomLevel, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }

            // Draw station dots
            line.stations.forEachIndexed { _, station ->
                val offset = stationToOffset(station)
                stationPositions[station.id] = offset

                val isSelected = selectedStation?.id == station.id
                val dotRadius = if (isSelected) 10.dp.toPx() * zoomLevel
                                else if (station.isInterchange) 8.dp.toPx() * zoomLevel
                                else 6.dp.toPx() * zoomLevel

                // White outline
                drawCircle(
                    color = Color.White,
                    radius = dotRadius + 2.dp.toPx() * zoomLevel,
                    center = offset
                )
                // Colored dot
                drawCircle(
                    color = if (isSelected) Turmeric else lineColor,
                    radius = dotRadius,
                    center = offset
                )
                // Interchange inner ring
                if (station.isInterchange && !isSelected) {
                    drawCircle(
                        color = Color.White,
                        radius = dotRadius * 0.5f,
                        center = offset
                    )
                }

                // Station label
                drawStationLabel(
                    name = station.name,
                    offset = offset,
                    zoomLevel = zoomLevel,
                    isSelected = isSelected,
                    textColor = if (isSelected) Turmeric else TextDark
                )
            }
        }
    }
}

// Draw station name as text on canvas
private fun DrawScope.drawStationLabel(
    name: String,
    offset: Offset,
    zoomLevel: Float,
    isSelected: Boolean,
    textColor: Color
) {
    val textSize = (10f * zoomLevel).coerceIn(8f, 16f)
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.argb(
            (textColor.alpha * 255).toInt(),
            (textColor.red * 255).toInt(),
            (textColor.green * 255).toInt(),
            (textColor.blue * 255).toInt()
        )
        this.textSize = textSize * density
        isAntiAlias = true
        typeface = if (isSelected) android.graphics.Typeface.DEFAULT_BOLD else android.graphics.Typeface.DEFAULT
    }
    val textWidth = paint.measureText(name)
    drawContext.canvas.nativeCanvas.drawText(
        name,
        offset.x - textWidth / 2,
        offset.y - 10.dp.toPx() * zoomLevel,
        paint
    )
}

// ── Interactive Map Info Card (shown when no station selected) ─────────────────

@Composable
fun InteractiveMapCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Interactive Map",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Tap on any station to view details,\nfacilities, and real-time status.",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextMedium),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            // Corridor legend chips
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CorridorChip(name = "Corridor 1", color = IndigoBlue)
                CorridorChip(name = "Corridor 2", color = VermilionRed)
            }
        }
    }
}

@Composable
fun CorridorChip(name: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.1f),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                name,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

// ── Station Detail Card ───────────────────────────────────────────────────────

@Suppress("UNUSED_PARAMETER")
@Composable
fun StationDetailCard(
    station: Station,
    corridorColor: Color,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Handle bar
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(DividerColor)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(16.dp))

            // Station name + corridor badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        station.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = corridorColor.copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(corridorColor)
                            )
                            Text(
                                if (station.corridor == 1) "Corridor 1" else "Corridor 2",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = corridorColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }

                // Interchange badge
                if (station.isInterchange) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Turmeric.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Filled.Train, contentDescription = null, tint = Turmeric, modifier = Modifier.size(14.dp))
                            Text(
                                "Interchange",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Turmeric,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }

            // Facilities
            if (station.facilities.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text(
                    "Facilities",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TextMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    station.facilities.forEach { facility ->
                        FacilityChip(facility)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2E7D32))
                )
                Text(
                    "Station Active — Normal Service",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
fun FacilityChip(name: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = ParchmentDark
    ) {
        Text(
            name,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextMedium,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

// ── Zoom Button ───────────────────────────────────────────────────────────────

@Composable
fun ZoomButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(44.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = SurfaceWhite,
        shadowElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            icon()
        }
    }
}

