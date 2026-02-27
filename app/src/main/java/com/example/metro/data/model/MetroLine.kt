package com.example.metro.data.model

import androidx.compose.ui.graphics.Color

data class MetroLine(
    val id: Int,
    val name: String,
    val color: Color,
    val stations: List<Station>
)

