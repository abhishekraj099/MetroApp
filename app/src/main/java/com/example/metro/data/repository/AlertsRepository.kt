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
            title = "Metro Operating Hours",
            subtitle = "Daily Schedule",
            description = "Patna Metro services operate from 6:00 AM to 10:00 PM on all days. " +
                    "First train departs at 6:00 AM from both terminal stations. " +
                    "Last train departs at 9:30 PM.",
            type = AlertType.INFO,
            category = AlertCategory.SERVICE_INFO
        ),
        MetroAlert(
            id = "s2",
            title = "Train Frequency",
            subtitle = "Peak & Off-Peak Hours",
            description = "Peak hours (8–10 AM & 5–8 PM): Trains every 5 minutes.\n" +
                    "Off-peak hours: Trains every 10 minutes.\n" +
                    "Sundays & holidays: Trains every 12 minutes.",
            type = AlertType.INFO,
            category = AlertCategory.SERVICE_INFO
        ),
        MetroAlert(
            id = "s3",
            title = "Fare Information",
            subtitle = "Token & Smart Card",
            description = "Minimum fare: ₹10 (up to 2 km).\n" +
                    "Maximum fare: ₹50 (full corridor).\n" +
                    "Smart Card users get 10% discount on all journeys. " +
                    "Cards available at all station counters.",
            type = AlertType.INFO,
            category = AlertCategory.SERVICE_INFO
        ),
        MetroAlert(
            id = "s4",
            title = "Emergency Helpline",
            subtitle = "24×7 Available",
            description = "Patna Metro Helpline: 0612-XXXXXXX\n" +
                    "Railway Police: 182\n" +
                    "Women Helpline: 1091\n" +
                    "For any emergency inside the metro, press the emergency button " +
                    "available in every coach or contact station staff.",
            type = AlertType.HELPLINE,
            category = AlertCategory.SERVICE_INFO
        ),
        MetroAlert(
            id = "s5",
            title = "Metro Rules & Guidelines",
            subtitle = "Important for Passengers",
            description = "• No smoking, eating, or drinking inside trains & stations.\n" +
                    "• Keep luggage within 15 kg limit.\n" +
                    "• Priority seating for elderly, disabled & pregnant women.\n" +
                    "• Do not obstruct train doors.\n" +
                    "• Inflammable & hazardous goods are strictly prohibited.\n" +
                    "• Pets not allowed (except guide dogs).",
            type = AlertType.RULE,
            category = AlertCategory.SERVICE_INFO
        ),
        MetroAlert(
            id = "s6",
            title = "Smart Card Recharge Points",
            subtitle = "Convenient Top-Up",
            description = "Smart Cards can be recharged at:\n" +
                    "• All metro station ticket counters\n" +
                    "• Automatic Ticket Vending Machines (ATVMs)\n" +
                    "• Authorized retail outlets near stations\n" +
                    "Minimum recharge: ₹100 | Maximum balance: ₹3,000",
            type = AlertType.TIP,
            category = AlertCategory.SERVICE_INFO
        ),
        MetroAlert(
            id = "s7",
            title = "Accessibility Features",
            subtitle = "Inclusive Metro for All",
            description = "All stations equipped with:\n" +
                    "• Lifts & escalators\n" +
                    "• Tactile paths for visually impaired\n" +
                    "• Wheelchair-accessible coaches\n" +
                    "• Braille signage at ticket counters\n" +
                    "• Dedicated help desks at major stations",
            type = AlertType.INFO,
            category = AlertCategory.SERVICE_INFO
        )
    )

    fun getPlannedWorksAlerts(): List<MetroAlert> = listOf(
        MetroAlert(
            id = "p1",
            title = "Corridor 2 Construction Update",
            subtitle = "Under Development",
            description = "Corridor 2 (North-South line) is currently under construction. " +
                    "Expected completion in phases. Stations like Patliputra and New ISBT " +
                    "are being developed with modern amenities. " +
                    "Check PMRC website for latest updates.",
            type = AlertType.ANNOUNCEMENT,
            category = AlertCategory.PLANNED_WORKS
        ),
        MetroAlert(
            id = "p2",
            title = "New Parking Facility at Danapur",
            subtitle = "Coming Soon",
            description = "A multi-level parking facility with 500+ vehicle capacity " +
                    "is being constructed at Danapur station. This will include " +
                    "two-wheeler parking, EV charging stations, and direct skyway " +
                    "access to the metro concourse.",
            type = AlertType.ANNOUNCEMENT,
            category = AlertCategory.PLANNED_WORKS
        ),
        MetroAlert(
            id = "p3",
            title = "Wi-Fi at All Stations",
            subtitle = "Rollout in Progress",
            description = "Free Wi-Fi service is being rolled out across all Corridor 1 stations. " +
                    "Currently available at Patna Junction and Gandhi Maidan. " +
                    "Full network coverage expected by mid-2026.",
            type = AlertType.INFO,
            category = AlertCategory.PLANNED_WORKS
        ),
        MetroAlert(
            id = "p4",
            title = "Feeder Bus Integration",
            subtitle = "Improving Last-Mile Connectivity",
            description = "PMRC is working with Bihar State Road Transport Corporation " +
                    "to launch feeder bus services connecting metro stations to nearby " +
                    "residential areas, hospitals, and educational institutions. " +
                    "Integrated ticketing will be available via Smart Card.",
            type = AlertType.ANNOUNCEMENT,
            category = AlertCategory.PLANNED_WORKS
        ),
        MetroAlert(
            id = "p5",
            title = "Travel Tips for Commuters",
            subtitle = "Save Time & Money",
            description = "• Use Smart Card for faster entry — avoid token queues.\n" +
                    "• Travel during off-peak hours (11 AM–4 PM) for a relaxed journey.\n" +
                    "• Patna Junction is an interchange — switch lines without buying a new ticket.\n" +
                    "• Keep your token/card handy at exit gates to avoid delays.\n" +
                    "• Download this app for quick fare calculation!",
            type = AlertType.TIP,
            category = AlertCategory.PLANNED_WORKS
        )
    )

    fun getAllAlerts(): List<MetroAlert> = getServiceInfoAlerts() + getPlannedWorksAlerts()
}

