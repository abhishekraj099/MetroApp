package com.example.metro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MetroColorScheme = lightColorScheme(
    primary          = VermilionRed,
    onPrimary        = SurfaceWhite,
    secondary        = IndigoBlue,
    onSecondary      = SurfaceWhite,
    tertiary         = Turmeric,
    onTertiary       = TextDark,
    background       = Parchment,
    onBackground     = TextDark,
    surface          = SurfaceWhite,
    onSurface        = TextDark,
    surfaceVariant   = ParchmentDark,
    onSurfaceVariant = TextMedium,
    outline          = DividerColor,
    error            = VermilionRed,
    onError          = SurfaceWhite,
)

@Composable
fun MetroTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MetroColorScheme,
        typography  = Typography,
        content     = content
    )
}