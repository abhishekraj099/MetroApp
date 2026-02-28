package com.example.metro.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Accessible
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.metro.data.model.Station
import com.example.metro.data.repository.MetroRepository
import com.example.metro.ui.theme.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.math.*

// ═══════════════════════════════════════════════════════════════════════════════
// 1. FARE CALCULATOR
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun FareCalculatorOverlay(
    fareFrom: String,
    fareTo: String,
    fareResult: Int?,
    fareStationCount: Int?,
    onFromClick: () -> Unit,
    onToClick: () -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Parchment)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, "Close", tint = TextDark)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "Fare Calculator",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = TextDark, fontWeight = FontWeight.Bold
                    )
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Station selectors
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Select Stations",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold, color = TextDark
                                )
                            )
                            Spacer(Modifier.height(12.dp))
                            StationClickableField(
                                label = "From Station",
                                value = fareFrom,
                                placeholder = "Select origin",
                                onClick = onFromClick
                            )
                            Spacer(Modifier.height(8.dp))
                            StationClickableField(
                                label = "To Station",
                                value = fareTo,
                                placeholder = "Select destination",
                                onClick = onToClick
                            )
                        }
                    }
                }

                // Fare result
                if (fareResult != null && fareStationCount != null) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = VermilionRed.copy(alpha = 0.08f)),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Your Fare",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = TextMedium, fontWeight = FontWeight.Medium
                                    )
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "₹$fareResult",
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        color = VermilionRed, fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "$fareStationCount stations • ~${(fareStationCount - 1) * 2} min",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = TextMedium)
                                )
                            }
                        }
                    }
                }

                // Fare chart
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Patna Metro Fare Chart",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold, color = TextDark
                                )
                            )
                            Text(
                                "Based on number of stations travelled",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextLight)
                            )
                            Spacer(Modifier.height(12.dp))

                            val fareSlabs = listOf(
                                "1 – 2 stations" to "₹10",
                                "3 – 5 stations" to "₹15",
                                "6 – 8 stations" to "₹20",
                                "9 – 12 stations" to "₹25",
                                "13 – 16 stations" to "₹30",
                                "17+ stations" to "₹35"
                            )
                            fareSlabs.forEachIndexed { index, (range, fare) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (index % 2 == 0) ParchmentDark.copy(alpha = 0.5f)
                                            else Color.Transparent,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        range,
                                        style = MaterialTheme.typography.bodyMedium.copy(color = TextDark)
                                    )
                                    Text(
                                        fare,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = VermilionRed, fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Info note
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = IndigoBlue.copy(alpha = 0.08f)),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Icon(
                                Icons.Outlined.Info, null,
                                tint = IndigoBlue, modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Fares are approximate and based on PMRC proposed fare structure. " +
                                        "Actual fares may vary once metro operations begin. " +
                                        "Smart card users may get up to 10% discount.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = IndigoBlue, lineHeight = 16.sp
                                )
                            )
                        }
                    }
                }

                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// 2. METRO TIMINGS
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun TimingsOverlay(onDismiss: () -> Unit) {
    BackHandler(onBack = onDismiss)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Parchment)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, "Close", tint = TextDark)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "Metro Timings",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = TextDark, fontWeight = FontWeight.Bold
                    )
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Operating hours
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Schedule, null, tint = VermilionRed, modifier = Modifier.size(22.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Operating Hours",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold, color = TextDark
                                    )
                                )
                            }
                            Spacer(Modifier.height(16.dp))

                            TimingInfoRow("First Train", "6:00 AM", Icons.Outlined.WbSunny)
                            TimingInfoRow("Last Train", "10:00 PM", Icons.Outlined.NightsStay)
                            TimingInfoRow("Service Duration", "6:00 AM – 10:00 PM", Icons.Outlined.AccessTime)
                        }
                    }
                }

                // Frequency
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Speed, null, tint = IndigoBlue, modifier = Modifier.size(22.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Train Frequency",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold, color = TextDark
                                    )
                                )
                            }
                            Spacer(Modifier.height(16.dp))

                            FrequencyCard(
                                "Peak Hours", "Every 5 – 10 min",
                                "8:00 AM – 10:00 AM\n5:00 PM – 8:00 PM",
                                VermilionRed
                            )
                            Spacer(Modifier.height(10.dp))
                            FrequencyCard(
                                "Off-Peak Hours", "Every 10 – 15 min",
                                "10:00 AM – 5:00 PM\n8:00 PM – 10:00 PM",
                                IndigoBlue
                            )
                            Spacer(Modifier.height(10.dp))
                            FrequencyCard(
                                "Early Morning", "Every 12 – 15 min",
                                "6:00 AM – 8:00 AM",
                                Color(0xFF2E7D32)
                            )
                        }
                    }
                }

                // Line-wise details
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Line-wise Details",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold, color = TextDark
                                )
                            )
                            Spacer(Modifier.height(12.dp))

                            LineTimingCard(
                                "Red Line — Corridor 1",
                                "Danapur ↔ Mithapur Bus Stand",
                                "11 Stations • ~22 min end-to-end",
                                VermilionRed
                            )
                            Spacer(Modifier.height(10.dp))
                            LineTimingCard(
                                "Blue Line — Corridor 2",
                                "Patna Junction ↔ New ISBT",
                                "9 Stations • ~18 min end-to-end",
                                IndigoBlue
                            )
                        }
                    }
                }

                // Note
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = IndigoBlue.copy(alpha = 0.08f)),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Icon(Icons.Outlined.Info, null, tint = IndigoBlue, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Timings are based on PMRC planned schedule. " +
                                        "Actual timings may vary. Sunday/Holiday timings may differ. " +
                                        "Last entry is 15 minutes before the last train.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = IndigoBlue, lineHeight = 16.sp
                                )
                            )
                        }
                    }
                }

                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun TimingInfoRow(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = TextLight, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium.copy(color = TextMedium), modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(color = TextDark, fontWeight = FontWeight.SemiBold))
    }
}

@Composable
private fun FrequencyCard(title: String, frequency: String, hours: String, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.labelLarge.copy(color = color, fontWeight = FontWeight.Bold))
                Text(hours, style = MaterialTheme.typography.bodySmall.copy(color = TextMedium, lineHeight = 16.sp))
            }
            Text(frequency, style = MaterialTheme.typography.bodyMedium.copy(color = color, fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
private fun LineTimingCard(name: String, route: String, info: String, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.06f)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
                Spacer(Modifier.width(8.dp))
                Text(name, style = MaterialTheme.typography.labelLarge.copy(color = color, fontWeight = FontWeight.Bold))
            }
            Spacer(Modifier.height(4.dp))
            Text(route, style = MaterialTheme.typography.bodyMedium.copy(color = TextDark))
            Text(info, style = MaterialTheme.typography.bodySmall.copy(color = TextMedium))
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// 3. NEAREST STATION
// ═══════════════════════════════════════════════════════════════════════════════

@SuppressLint("MissingPermission")
@Composable
fun NearestStationOverlay(onDismiss: () -> Unit) {
    BackHandler(onBack = onDismiss)

    val context = LocalContext.current
    val metroRepo = remember { MetroRepository() }
    val allStations = remember { metroRepo.getAllStations() }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }
    var userLat by remember { mutableDoubleStateOf(0.0) }
    var userLon by remember { mutableDoubleStateOf(0.0) }
    var isLoading by remember { mutableStateOf(true) }
    var locationError by remember { mutableStateOf("") }

    // Sorted stations by distance
    val sortedStations = remember(userLat, userLon) {
        if (userLat == 0.0 && userLon == 0.0) emptyList()
        else allStations.map { station ->
            station to haversineDistance(userLat, userLon, station.latitude, station.longitude)
        }.sortedBy { it.second }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (!granted) {
            isLoading = false
            locationError = "Location permission denied"
        }
    }

    // Get location
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            isLoading = true
            locationError = ""
            try {
                val client = LocationServices.getFusedLocationProviderClient(context)
                val cancellationToken = CancellationTokenSource()
                client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken.token)
                    .addOnSuccessListener { loc: android.location.Location? ->
                        if (loc != null) {
                            userLat = loc.latitude
                            userLon = loc.longitude
                            isLoading = false
                        } else {
                            // Try last known location
                            client.lastLocation.addOnSuccessListener { last: android.location.Location? ->
                                if (last != null) {
                                    userLat = last.latitude
                                    userLon = last.longitude
                                }
                                isLoading = false
                                if (last == null) locationError = "Could not get location. Try again."
                            }
                        }
                    }
                    .addOnFailureListener { ex: Exception ->
                        isLoading = false
                        locationError = "Location error: ${ex.message}"
                    }
            } catch (e: Exception) {
                isLoading = false
                locationError = "Error: ${e.message}"
            }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Parchment)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, "Close", tint = TextDark)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "Nearest Station",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = TextDark, fontWeight = FontWeight.Bold
                    )
                )
            }

            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = VermilionRed)
                            Spacer(Modifier.height(16.dp))
                            Text("Getting your location...", style = MaterialTheme.typography.bodyMedium.copy(color = TextMedium))
                        }
                    }
                }
                locationError.isNotBlank() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.LocationOff, null, tint = VermilionRed, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(12.dp))
                            Text(locationError, style = MaterialTheme.typography.bodyMedium.copy(color = TextMedium), textAlign = TextAlign.Center)
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                                colors = ButtonDefaults.buttonColors(containerColor = VermilionRed)
                            ) {
                                Text("Grant Permission", color = Color.White)
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Nearest station highlighted
                        if (sortedStations.isNotEmpty()) {
                            item {
                                val (station, distance) = sortedStations.first()
                                val lineColor = if ("RED" in station.lines) VermilionRed else IndigoBlue
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = lineColor.copy(alpha = 0.08f)),
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Filled.NearMe, null, tint = lineColor, modifier = Modifier.size(24.dp))
                                            Spacer(Modifier.width(8.dp))
                                            Text("Nearest to You", style = MaterialTheme.typography.titleMedium.copy(color = lineColor, fontWeight = FontWeight.Bold))
                                        }
                                        Spacer(Modifier.height(12.dp))
                                        Text(
                                            station.name,
                                            style = MaterialTheme.typography.headlineSmall.copy(color = TextDark, fontWeight = FontWeight.Bold)
                                        )
                                        if (station.nameHindi.isNotEmpty()) {
                                            Text(station.nameHindi, style = MaterialTheme.typography.bodyMedium.copy(color = TextMedium))
                                        }
                                        Spacer(Modifier.height(8.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                            NearestChip(Icons.Filled.SocialDistance, "%.1f km".format(distance))
                                            NearestChip(Icons.Filled.Train, if ("RED" in station.lines && "BLUE" in station.lines) "Red + Blue" else if ("RED" in station.lines) "Red Line" else "Blue Line")
                                        }
                                        if (station.facilities.isNotEmpty()) {
                                            Spacer(Modifier.height(8.dp))
                                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                station.facilities.forEach { facility ->
                                                    FacilityChip(facility)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                Text(
                                    "All Stations by Distance",
                                    style = MaterialTheme.typography.titleMedium.copy(color = TextDark, fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }

                        // All stations sorted by distance
                        items(sortedStations.drop(1)) { (station, distance) ->
                            val lineColor = if ("RED" in station.lines) VermilionRed else IndigoBlue
                            NearbyStationRow(station, distance, lineColor)
                        }

                        item { Spacer(Modifier.height(16.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun NearestChip(icon: ImageVector, text: String) {
    Surface(shape = RoundedCornerShape(20.dp), color = ParchmentDark) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = TextMedium, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(4.dp))
            Text(text, style = MaterialTheme.typography.labelSmall.copy(color = TextDark, fontWeight = FontWeight.Medium))
        }
    }
}

@Composable
private fun NearbyStationRow(station: Station, distance: Double, lineColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(lineColor)
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(station.name, style = MaterialTheme.typography.bodyLarge.copy(color = TextDark, fontWeight = FontWeight.Medium))
                if (station.nameHindi.isNotEmpty()) {
                    Text(station.nameHindi, style = MaterialTheme.typography.bodySmall.copy(color = TextLight))
                }
            }
            Text(
                "%.1f km".format(distance),
                style = MaterialTheme.typography.labelMedium.copy(color = lineColor, fontWeight = FontWeight.Bold)
            )
        }
    }
}

/** Haversine formula — distance in km between two lat/lon points */
private fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val r = 6371.0 // Earth radius km
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
    val c = 2 * asin(sqrt(a))
    return r * c
}

// ═══════════════════════════════════════════════════════════════════════════════
// 4. ACCESSIBILITY INFO
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun AccessibilityOverlay(onDismiss: () -> Unit) {
    BackHandler(onBack = onDismiss)

    val metroRepo = remember { MetroRepository() }
    val allStations = remember { metroRepo.getAllStations() }

    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Lift", "Parking", "Wheelchair")

    val filteredStations = remember(selectedFilter) {
        if (selectedFilter == "All") allStations
        else allStations.filter { selectedFilter in it.facilities }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Parchment)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, "Close", tint = TextDark)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "Accessibility",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = TextDark, fontWeight = FontWeight.Bold
                    )
                )
            }

            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                filter,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        leadingIcon = {
                            when (filter) {
                                "Lift" -> Icon(Icons.Outlined.Elevator, null, modifier = Modifier.size(16.dp))
                                "Parking" -> Icon(Icons.Outlined.LocalParking, null, modifier = Modifier.size(16.dp))
                                "Wheelchair" -> Icon(Icons.AutoMirrored.Outlined.Accessible, null, modifier = Modifier.size(16.dp))
                                else -> Icon(Icons.Outlined.FilterList, null, modifier = Modifier.size(16.dp))
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = VermilionRed.copy(alpha = 0.12f),
                            selectedLabelColor = VermilionRed,
                            selectedLeadingIconColor = VermilionRed
                        )
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                "${filteredStations.size} stations",
                style = MaterialTheme.typography.bodySmall.copy(color = TextLight),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredStations) { station ->
                    val lineColor = if ("RED" in station.lines) VermilionRed else IndigoBlue
                    AccessibilityStationCard(station, lineColor)
                }

                if (filteredStations.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Outlined.SearchOff, null, tint = TextLight, modifier = Modifier.size(40.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("No stations with $selectedFilter", style = MaterialTheme.typography.bodyMedium.copy(color = TextLight))
                        }
                    }
                }

                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun AccessibilityStationCard(station: Station, lineColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(lineColor))
                Spacer(Modifier.width(8.dp))
                Text(
                    station.name,
                    style = MaterialTheme.typography.bodyLarge.copy(color = TextDark, fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.weight(1f)
                )
                if (station.isInterchange) {
                    Surface(shape = RoundedCornerShape(8.dp), color = Turmeric.copy(alpha = 0.15f)) {
                        Text(
                            "Interchange",
                            style = MaterialTheme.typography.labelSmall.copy(color = Turmeric, fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            if (station.nameHindi.isNotEmpty()) {
                Text(station.nameHindi, style = MaterialTheme.typography.bodySmall.copy(color = TextLight))
            }

            if (station.facilities.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    station.facilities.forEach { facility ->
                        FacilityChip(facility)
                    }
                }
            } else {
                Spacer(Modifier.height(6.dp))
                Text(
                    "No accessibility facilities listed",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextLight)
                )
            }
        }
    }
}

@Composable
fun FacilityChip(facility: String) {
    val (icon, color) = when (facility) {
        "Lift" -> Icons.Outlined.Elevator to Color(0xFF6A1B9A)
        "Parking" -> Icons.Outlined.LocalParking to IndigoBlue
        "Wheelchair" -> Icons.AutoMirrored.Outlined.Accessible to Color(0xFF2E7D32)
        else -> Icons.Outlined.Info to TextMedium
    }
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(4.dp))
            Text(facility, style = MaterialTheme.typography.labelSmall.copy(color = color, fontWeight = FontWeight.Medium))
        }
    }
}

