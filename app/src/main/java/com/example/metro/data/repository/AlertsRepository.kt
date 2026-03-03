package com.example.metro.data.repository

import com.example.metro.data.model.AlertCategory
import com.example.metro.data.model.AlertType
import com.example.metro.data.model.MetroAlert

/**
 * Provides static alerts & information data for Patna Metro.
 * Since this is an unofficial guide, we show factual/useful info —
 * NOT real-time service predictions.
 */
class AlertsRepository {

    fun getServiceInfoAlerts(): List<MetroAlert> = listOf(
        MetroAlert(
            id = "s1",
            title = "Metro Operating Hours (Expected)",
            subtitle = "Based on similar Indian Metro systems",
            description = "Expected operating hours: 6:00 AM to 10:00 PM daily. " +
                    "First train expected at 6:00 AM from terminal stations. " +
                    "Last train expected at 9:30 PM.\n\n" +
                    "Note: Actual timings will be confirmed by PMRC once operations begin.",
            type = AlertType.INFO,
            category = AlertCategory.SERVICE_INFO
        ),
        MetroAlert(
            id = "s2",
            title = "Expected Train Frequency",
            subtitle = "Approximate · Based on DPR",
            description = "Expected frequency once operational:\n" +
                    "Peak hours (8–10 AM & 5–8 PM): Every 5–10 minutes.\n" +
                    "Off-peak hours: Every 10–15 minutes.\n" +
                    "Sundays & holidays: Every 12–15 minutes.\n\n" +
                    "Note: Frequency will be confirmed by PMRC.",
            type = AlertType.INFO,
            category = AlertCategory.SERVICE_INFO
        ),
        MetroAlert(
            id = "s3",
            title = "Approximate Fare Information",
            subtitle = "Estimated · Not yet confirmed",
            description = "Estimated fare range (based on DPR):\n" +
                    "Minimum fare: ~₹10 (up to 2 stations).\n" +
                    "Maximum fare: ~₹35 (full corridor).\n\n" +
                    "Note: Actual fares will be announced by PMRC before launch. " +
                    "Smart Card discounts are common in Indian metros.",
            type = AlertType.INFO,
            category = AlertCategory.SERVICE_INFO
        ),
        MetroAlert(
            id = "s4",
            title = "Emergency Helpline Numbers",
            subtitle = "General Emergency Services",
            description = "General Emergency: 112\n" +
                    "Railway Helpline: 139\n" +
                    "Railway Police (GRP): 182\n" +
                    "Women Helpline: 1091\n" +
                    "Ambulance: 108\n\n" +
                    "Note: Patna Metro helpline number will be available once operations begin. " +
                    "Visit www.patna-metro.com for updates.",
            type = AlertType.HELPLINE,
            category = AlertCategory.SERVICE_INFO
        ),
        MetroAlert(
            id = "s5",
            title = "General Metro Guidelines",
            subtitle = "Standard Rules in Indian Metros",
            description = "Standard metro guidelines (common across Indian metros):\n" +
                    "• No smoking, eating, or drinking inside trains & stations.\n" +
                    "• Keep luggage within specified limits.\n" +
                    "• Priority seating for elderly, disabled & pregnant women.\n" +
                    "• Do not obstruct train doors.\n" +
                    "• Inflammable & hazardous goods are prohibited.\n\n" +
                    "Note: Patna Metro specific rules will be published by PMRC.",
            type = AlertType.RULE,
            category = AlertCategory.SERVICE_INFO
        ),
        MetroAlert(
            id = "s6",
            title = "Ticketing Information (Expected)",
            subtitle = "Based on similar Indian Metros",
            description = "Expected ticketing options:\n" +
                    "• Token-based single journey tickets\n" +
                    "• Rechargeable Smart Cards\n" +
                    "• Automatic Ticket Vending Machines (ATVMs)\n\n" +
                    "Note: Exact ticketing details will be confirmed by PMRC. " +
                    "Most Indian metros offer Smart Card discounts.",
            type = AlertType.TIP,
            category = AlertCategory.SERVICE_INFO
        ),
        MetroAlert(
            id = "s7",
            title = "Planned Accessibility Features",
            subtitle = "As per DPR · Subject to change",
            description = "As per the Detailed Project Report, stations are planned with:\n" +
                    "• Lifts & escalators\n" +
                    "• Tactile paths for visually impaired\n" +
                    "• Wheelchair-accessible coaches\n" +
                    "• Braille signage\n\n" +
                    "Note: Features are based on PMRC DPR and standard metro norms.",
            type = AlertType.INFO,
            category = AlertCategory.SERVICE_INFO
        )
    )

    fun getPlannedWorksAlerts(): List<MetroAlert> = listOf(
        MetroAlert(
            id = "p1",
            title = "Patna Metro — Under Construction",
            subtitle = "Project Status",
            description = "Patna Metro is currently under construction by PMRC. " +
                    "The project includes two corridors connecting major areas of Patna. " +
                    "For the latest construction updates and timelines, " +
                    "please visit the PMRC website: www.patna-metro.com",
            type = AlertType.ANNOUNCEMENT,
            category = AlertCategory.PLANNED_WORKS
        ),
        MetroAlert(
            id = "p2",
            title = "Corridor 1 — Danapur to Mithapur",
            subtitle = "East–West Line (Red Line)",
            description = "Corridor 1 covers the East–West route with 11 stations: " +
                    "Danapur, Saguna More, RPS More, Rukanpura, Raja Bazar, " +
                    "Patna Junction (interchange), Frazer Road, Gandhi Maidan, " +
                    "PMCH, Rajendra Nagar, and Mithapur Bus Stand. " +
                    "Data based on PMRC DPR (Detailed Project Report).",
            type = AlertType.INFO,
            category = AlertCategory.PLANNED_WORKS
        ),
        MetroAlert(
            id = "p3",
            title = "Corridor 2 — Patna Junction to New ISBT",
            subtitle = "North–South Line (Blue Line)",
            description = "Corridor 2 covers the North–South route with 9 stations: " +
                    "Patna Junction (interchange), Akashvani, Secretariat, " +
                    "Vidyut Bhawan, Shri Krishna Nagar, Patliputra, " +
                    "Jaganpura, Khemnichak, and New ISBT. " +
                    "Data based on PMRC DPR (Detailed Project Report).",
            type = AlertType.INFO,
            category = AlertCategory.PLANNED_WORKS
        ),
        MetroAlert(
            id = "p4",
            title = "About This App",
            subtitle = "Disclaimer",
            description = "This is an UNOFFICIAL companion app and is NOT affiliated " +
                    "with Patna Metro Rail Corporation (PMRC) or the Government of Bihar. " +
                    "Station names, fares, and timings shown here are approximate and " +
                    "based on publicly available DPR information. " +
                    "Always verify from PMRC for the latest and accurate details.",
            type = AlertType.ANNOUNCEMENT,
            category = AlertCategory.PLANNED_WORKS
        ),
        MetroAlert(
            id = "p5",
            title = "Tips for Future Commuters",
            subtitle = "Useful When Metro Launches",
            description = "• Smart Cards are usually faster than token queues in Indian metros.\n" +
                    "• Off-peak travel (11 AM–4 PM) is generally less crowded.\n" +
                    "• Patna Junction will be the interchange station between both lines.\n" +
                    "• Keep your token/card ready at exit gates.\n" +
                    "• Use this app to plan your route and estimate fares!",
            type = AlertType.TIP,
            category = AlertCategory.PLANNED_WORKS
        )
    )

    fun getAllAlerts(): List<MetroAlert> = getServiceInfoAlerts() + getPlannedWorksAlerts()
}

