package com.example.metro.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    // ── Display (Lora) ────────────────────────────────────────────────────
    displayLarge = TextStyle(
        fontFamily = LoraFont, fontWeight = FontWeight.Bold,
        fontSize = 48.sp, lineHeight = 56.sp, letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = LoraFont, fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp, lineHeight = 44.sp
    ),
    displaySmall = TextStyle(
        fontFamily = LoraFont, fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp, lineHeight = 36.sp
    ),
    // ── Headlines (Lora) ──────────────────────────────────────────────────
    headlineLarge = TextStyle(
        fontFamily = LoraFont, fontWeight = FontWeight.Bold,
        fontSize = 24.sp, lineHeight = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = LoraFont, fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp, lineHeight = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = LoraFont, fontWeight = FontWeight.Medium,
        fontSize = 18.sp, lineHeight = 26.sp
    ),
    // ── Titles (Outfit) ───────────────────────────────────────────────────
    titleLarge = TextStyle(
        fontFamily = OutfitFont, fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp, lineHeight = 26.sp
    ),
    titleMedium = TextStyle(
        fontFamily = OutfitFont, fontWeight = FontWeight.Medium,
        fontSize = 16.sp, lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = OutfitFont, fontWeight = FontWeight.Medium,
        fontSize = 14.sp, lineHeight = 20.sp
    ),
    // ── Body (Outfit) ─────────────────────────────────────────────────────
    bodyLarge = TextStyle(
        fontFamily = OutfitFont, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = OutfitFont, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = OutfitFont, fontWeight = FontWeight.Normal,
        fontSize = 12.sp, lineHeight = 16.sp
    ),
    // ── Labels (Outfit) ───────────────────────────────────────────────────
    labelLarge = TextStyle(
        fontFamily = OutfitFont, fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = OutfitFont, fontWeight = FontWeight.Medium,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = OutfitFont, fontWeight = FontWeight.Medium,
        fontSize = 10.sp, lineHeight = 14.sp, letterSpacing = 0.5.sp
    )
)