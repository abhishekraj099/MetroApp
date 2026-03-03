package com.example.metro.presentation.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.metro.ui.theme.DividerColor
import com.example.metro.ui.theme.IndigoBlue
import com.example.metro.ui.theme.Parchment
import com.example.metro.ui.theme.ParchmentDark
import com.example.metro.ui.theme.SurfaceWhite
import com.example.metro.ui.theme.TextDark
import com.example.metro.ui.theme.TextLight
import com.example.metro.ui.theme.TextMedium
import com.example.metro.ui.theme.Turmeric
import com.example.metro.ui.theme.VermilionRed

// ─── Map Screen ───────────────────────────────────────────────────────────────

@Composable
fun MapScreen(
    vm: MapViewModel = viewModel(),
    onNavigateToSettings: () -> Unit = {}
) {
    val state by vm.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        MapHeader(onNavigateToSettings = onNavigateToSettings)

        // Map area fills remaining space
        Box(modifier = Modifier.fillMaxSize()) {
            // Interactive canvas map
            InteractiveMetroMap(
                lines = state.lines,
                selectedStation = state.selectedStation,
                zoomLevel = state.zoomLevel,
                offsetX = state.offsetX,
                offsetY = state.offsetY,
                showCorridor1 = state.showCorridor1,
                showCorridor2 = state.showCorridor2,
                onStationTap = vm::onStationTapped,
                onZoomChange = vm::onZoomChange,
                onPanChange = vm::onPanChange
            )

            // Line toggle chips — top-left
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                LineToggleChip("Red Line", VermilionRed, state.showCorridor1, vm::toggleCorridor1)
                LineToggleChip("Blue Line", IndigoBlue, state.showCorridor2, vm::toggleCorridor2)
            }

            // Zoom controls — right center
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
                    .shadow(4.dp, RoundedCornerShape(14.dp))
                    .background(SurfaceWhite, RoundedCornerShape(14.dp))
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MapIconBtn(onClick = vm::resetView) {
                    Icon(Icons.Filled.MyLocation, "Reset", tint = IndigoBlue, modifier = Modifier.size(20.dp))
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 6.dp).width(28.dp),
                    color = DividerColor, thickness = 0.5.dp
                )
                MapIconBtn(onClick = vm::zoomIn) {
                    Icon(Icons.Outlined.Add, "Zoom In", tint = TextDark, modifier = Modifier.size(20.dp))
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 6.dp).width(28.dp),
                    color = DividerColor, thickness = 0.5.dp
                )
                MapIconBtn(onClick = vm::zoomOut) {
                    Icon(Icons.Outlined.Remove, "Zoom Out", tint = TextDark, modifier = Modifier.size(20.dp))
                }
            }

            // Station detail card — bottom sheet
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = state.selectedStation != null,
                    enter = fadeIn() + slideInVertically { it },
                    exit = fadeOut() + slideOutVertically { it }
                ) {
                    state.selectedStation?.let { station ->
                        StationDetailCard(
                            station = station,
                            corridorColor = if (station.corridor == 1) VermilionRed else IndigoBlue,
                            onDismiss = vm::dismissStationDetail
                        )
                    }
                }
            }

            // Hint text — bottom center (when nothing selected)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 14.dp)
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = state.selectedStation == null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = TextDark.copy(alpha = 0.7f)
                    ) {
                        Text(
                            "Tap a station \u2022 Pinch to zoom \u2022 Drag to pan",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White, fontSize = 11.sp
                            ),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// ── Interactive Metro Map ─────────────────────────────────────────────────────
// Uses `transformable` for pinch-zoom + pan, and `detectTapGestures` for taps.

@Composable
private fun InteractiveMetroMap(
    lines: List<MetroLine>,
    selectedStation: Station?,
    zoomLevel: Float,
    offsetX: Float,
    offsetY: Float,
    showCorridor1: Boolean,
    showCorridor2: Boolean,
    onStationTap: (Station) -> Unit,
    onZoomChange: (Float) -> Unit,
    onPanChange: (Float, Float) -> Unit
) {
    val allStations = remember(lines) { lines.flatMap { it.stations } }
    if (allStations.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().background(Parchment))
        return
    }

    val minLat = remember(allStations) { allStations.minOf { it.latitude } }
    val maxLat = remember(allStations) { allStations.maxOf { it.latitude } }
    val minLng = remember(allStations) { allStations.minOf { it.longitude } }
    val maxLng = remember(allStations) { allStations.maxOf { it.longitude } }

    // Updated every draw frame — used for tap hit-testing
    val stationPositions = remember { mutableStateMapOf<String, Offset>() }

    val transformState = rememberTransformableState { zoomDelta, panDelta, _ ->
        onZoomChange(zoomDelta)
        onPanChange(panDelta.x, panDelta.y)
    }

    Box(modifier = Modifier.fillMaxSize().background(Parchment)) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = transformState)
                .pointerInput(stationPositions.keys.toSet()) {
                    detectTapGestures { tapPos ->
                        val tapR = 40.dp.toPx()
                        val hit = stationPositions.entries.minByOrNull { (_, pos) ->
                            val dx = tapPos.x - pos.x; val dy = tapPos.y - pos.y
                            dx * dx + dy * dy
                        }
                        if (hit != null) {
                            val dx = tapPos.x - hit.value.x; val dy = tapPos.y - hit.value.y
                            if (dx * dx + dy * dy <= tapR * tapR) {
                                allStations.find { it.id == hit.key }?.let { onStationTap(it) }
                            }
                        }
                    }
                }
        ) {
            val w = size.width; val h = size.height
            val padH = w * 0.14f; val padV = h * 0.13f
            val drawW = w - 2 * padH; val drawH = h - 2 * padV
            val latR = (maxLat - minLat).coerceAtLeast(0.001)
            val lngR = (maxLng - minLng).coerceAtLeast(0.001)
            val z = zoomLevel.coerceIn(0.4f, 4f)

            fun toCanvas(s: Station): Offset {
                val nx = ((s.longitude - minLng) / lngR).toFloat()
                val ny = ((maxLat - s.latitude) / latR).toFloat()
                val bx = padH + nx * drawW; val by = padV + ny * drawH
                return Offset(
                    (bx - w / 2) * z + w / 2 + offsetX,
                    (by - h / 2) * z + h / 2 + offsetY
                )
            }

            // Grid
            val gridP = android.graphics.Paint().apply {
                color = android.graphics.Color.argb(15, 60, 60, 80); strokeWidth = 1f
            }
            for (i in 0..6) {
                val x = w / 6f * i; val y = h / 6f * i
                drawContext.canvas.nativeCanvas.drawLine(x, 0f, x, h, gridP)
                drawContext.canvas.nativeCanvas.drawLine(0f, y, w, y, gridP)
            }

            lines.forEach { line ->
                val show = if (line.id == 1) showCorridor1 else showCorridor2
                if (!show) return@forEach
                val lc = line.color
                val pts = line.stations.map { toCanvas(it) }

                // Line path
                if (pts.size >= 2) {
                    val path = Path().apply {
                        moveTo(pts[0].x, pts[0].y)
                        for (i in 1 until pts.size) {
                            val prev = pts[i - 1]; val cur = pts[i]
                            quadraticTo(prev.x, prev.y, (prev.x + cur.x) / 2f, (prev.y + cur.y) / 2f)
                        }
                        lineTo(pts.last().x, pts.last().y)
                    }
                    drawPath(path, lc.copy(alpha = 0.15f),
                        style = Stroke(16.dp.toPx() * z, cap = StrokeCap.Round, join = StrokeJoin.Round))
                    drawPath(path, lc,
                        style = Stroke(5.dp.toPx() * z, cap = StrokeCap.Round, join = StrokeJoin.Round))
                }

                // Station dots
                line.stations.forEach { station ->
                    val c = toCanvas(station)
                    stationPositions[station.id] = c
                    val sel = selectedStation?.id == station.id
                    val r = when {
                        sel -> 11.dp.toPx() * z
                        station.isInterchange -> 9.dp.toPx() * z
                        else -> 6.dp.toPx() * z
                    }

                    if (sel) {
                        drawCircle(Turmeric.copy(alpha = 0.15f), r * 2.6f, c)
                        drawCircle(Turmeric.copy(alpha = 0.3f), r * 1.9f, c, style = Stroke(2.dp.toPx()))
                    }
                    drawCircle(Color.White, r + 2.5.dp.toPx() * z, c)
                    drawCircle(if (sel) Turmeric else lc, r, c)
                    if (station.isInterchange && !sel) drawCircle(Color.White, r * 0.42f, c)

                    if (z >= 0.6f) drawLabel(station.name, c, z, sel, station.isInterchange)
                }
            }
        }
    }
}

// ── Draw station label on Canvas ──────────────────────────────────────────────

private fun DrawScope.drawLabel(
    name: String, center: Offset, zoom: Float,
    isSelected: Boolean, isInterchange: Boolean
) {
    val textSz = (8.5f * zoom * density).coerceIn(18f, 34f)
    val clearance = (13f * zoom * density).coerceIn(26f, 52f)
    val tp = android.graphics.Paint().apply {
        isAntiAlias = true; textSize = textSz
        textAlign = android.graphics.Paint.Align.CENTER
        typeface = if (isSelected || isInterchange)
            android.graphics.Typeface.DEFAULT_BOLD else android.graphics.Typeface.DEFAULT
        color = when {
            isSelected -> android.graphics.Color.argb(255, 160, 80, 0)
            isInterchange -> android.graphics.Color.argb(255, 26, 26, 46)
            else -> android.graphics.Color.argb(210, 26, 26, 46)
        }
    }
    val tw = tp.measureText(name); val pad = 5f
    val bg = android.graphics.Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.argb(if (isSelected) 230 else 190, 250, 247, 242)
    }
    drawContext.canvas.nativeCanvas.drawRoundRect(
        android.graphics.RectF(
            center.x - tw / 2 - pad, center.y - clearance - textSz - 1f,
            center.x + tw / 2 + pad, center.y - clearance + pad
        ), 6f, 6f, bg
    )
    drawContext.canvas.nativeCanvas.drawText(name, center.x, center.y - clearance, tp)
}

// ── Map Header ────────────────────────────────────────────────────────────────

@Composable
private fun MapHeader(onNavigateToSettings: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().statusBarsPadding().height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(R.drawable.newborder), null,
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(Parchment.copy(alpha = 0.85f)))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painterResource(R.drawable.newlogo), "Logo",
                    modifier = Modifier.size(48.dp), contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "System Map",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold, color = TextDark, fontSize = 20.sp
                        )
                    )
                    Text(
                        "UNOFFICIAL GUIDE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = VermilionRed, fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp, fontSize = 10.sp
                        )
                    )
                }
            }
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape)
                    .background(ParchmentDark.copy(alpha = 0.8f))
                    .clickable { onNavigateToSettings() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Settings, "Settings", tint = IndigoBlue, modifier = Modifier.size(22.dp))
            }
        }
    }
}

// ── Line Toggle Chip ──────────────────────────────────────────────────────────

@Composable
private fun LineToggleChip(label: String, color: Color, enabled: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (enabled) color else SurfaceWhite,
        shadowElevation = 3.dp,
        modifier = Modifier.height(30.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(modifier = Modifier.size(7.dp).clip(CircleShape)
                .background(if (enabled) Color.White else color))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (enabled) Color.White else color,
                    fontWeight = FontWeight.SemiBold, fontSize = 11.sp
                )
            )
        }
    }
}

// ── Map Icon Button ───────────────────────────────────────────────────────────

@Composable
private fun MapIconBtn(onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.size(40.dp).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { content() }
}

// ── Station Detail Card ───────────────────────────────────────────────────────

@Composable
private fun StationDetailCard(station: Station, corridorColor: Color, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp).padding(bottom = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Handle + close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.width(40.dp).height(4.dp)
                        .clip(RoundedCornerShape(2.dp)).background(DividerColor)
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Filled.Close, "Close", tint = TextLight, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.height(10.dp))

            // Name + badges
            Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        station.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold, color = TextDark, fontSize = 20.sp
                        )
                    )
                    if (station.nameHindi.isNotEmpty()) {
                        Text(station.nameHindi, style = MaterialTheme.typography.bodyMedium.copy(color = TextMedium))
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Corridor badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = corridorColor.copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(corridorColor))
                                Text(
                                    if (station.corridor == 1) "Red Line \u00B7 C1" else "Blue Line \u00B7 C2",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = corridorColor, fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                        // Interchange badge
                        if (station.isInterchange) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Turmeric.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Filled.Train, null, tint = Turmeric, modifier = Modifier.size(12.dp))
                                    Text(
                                        "Interchange",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Turmeric, fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Station index
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = IndigoBlue.copy(alpha = 0.08f)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "#${station.index + 1}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold, color = IndigoBlue, fontSize = 28.sp
                            )
                        )
                        Text("Station", style = MaterialTheme.typography.labelSmall.copy(color = TextMedium, fontSize = 11.sp))
                    }
                }
            }

            // Facilities
            if (station.facilities.isNotEmpty()) {
                Spacer(Modifier.height(14.dp))
                HorizontalDivider(color = DividerColor)
                Spacer(Modifier.height(10.dp))
                Text(
                    "FACILITIES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextLight, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    station.facilities.forEach { f ->
                        Surface(shape = RoundedCornerShape(8.dp), color = ParchmentDark) {
                            Text(
                                f,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMedium, fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Status bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
                    .background(Color(0xFFFFF3E0), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE65100)))
                Text(
                    "Under Construction \u2014 Data based on DPR",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFE65100), fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

