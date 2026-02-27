package com.example.metro.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.metro.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage   = "com.google.android.gms",
    certificates      = R.array.com_google_android_gms_fonts_certs
)

// Outfit — UI / functional text
val OutfitFont = FontFamily(
    Font(GoogleFont("Outfit"), provider, FontWeight.Light),
    Font(GoogleFont("Outfit"), provider, FontWeight.Normal),
    Font(GoogleFont("Outfit"), provider, FontWeight.Medium),
    Font(GoogleFont("Outfit"), provider, FontWeight.SemiBold),
    Font(GoogleFont("Outfit"), provider, FontWeight.Bold),
    Font(GoogleFont("Outfit"), provider, FontWeight.ExtraBold)
)

// Lora — display / headings
val LoraFont = FontFamily(
    Font(GoogleFont("Lora"), provider, FontWeight.Normal),
    Font(GoogleFont("Lora"), provider, FontWeight.Normal, FontStyle.Italic),
    Font(GoogleFont("Lora"), provider, FontWeight.Medium),
    Font(GoogleFont("Lora"), provider, FontWeight.SemiBold),
    Font(GoogleFont("Lora"), provider, FontWeight.Bold)
)
