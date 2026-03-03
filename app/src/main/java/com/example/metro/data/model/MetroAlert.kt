package com.example.metro.data.model


/**
 * Represents an info/alert item in the Alerts & Info section.
 */
data class MetroAlert(
    val id: String,
    val title: String,
    val subtitle: String,          // e.g. "Updated 2h ago", "Effective from Jan 2026"
    val description: String,
    val type: AlertType,
    val category: AlertCategory
)

enum class AlertType {
    INFO,           // Blue ℹ  — general information
    ANNOUNCEMENT,   // Turmeric 📢 — general announcements
    RULE,           // Indigo 📋 — metro rules & guidelines
    HELPLINE,       // Green 📞 — emergency / helpline
    TIP             // Vermilion 💡 — travel tips
}

enum class AlertCategory {
    SERVICE_INFO,
    PLANNED_WORKS
}

