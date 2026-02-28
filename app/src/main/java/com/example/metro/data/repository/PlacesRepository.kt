package com.example.metro.data.repository

import com.example.metro.data.model.PatnaPlace
import com.example.metro.data.model.PlaceCategory

/**
 * Provides curated list of tourist places in Patna accessible via metro.
 */
class PlacesRepository {

    fun getAllPlaces(): List<PatnaPlace> = listOf(
        PatnaPlace(
            id = "p1",
            name = "Patna Sahib Gurudwara",
            nameHindi = "पटना साहिब गुरुद्वारा",
            description = "Takht Sri Patna Sahib is one of the five Takhts (seats of authority) " +
                    "of the Sikh religion. It marks the birthplace of Guru Gobind Singh Ji, the " +
                    "tenth Sikh Guru. The gurudwara is a stunning white marble structure on the " +
                    "banks of the Ganges, attracting pilgrims and tourists from around the world. " +
                    "The complex houses a museum with historical Sikh artifacts and weapons.",
            nearestStation = "Patna Junction",
            distance = "1.2 km from Patna Junction station",
            timings = "4:00 AM – 10:00 PM (Daily)",
            entryFee = "Free",
            category = PlaceCategory.RELIGIOUS,
            highlights = listOf("Sikh Takht", "Ganges View", "Museum", "Langar"),
            emoji = "🛕"
        ),
        PatnaPlace(
            id = "p2",
            name = "Patna Zoo (Sanjay Gandhi Jaivik Udyan)",
            nameHindi = "पटना चिड़ियाघर (संजय गांधी जैविक उद्यान)",
            description = "Spread over 153 acres, Sanjay Gandhi Jaivik Udyan is one of the largest " +
                    "zoos in India. Home to over 800 animals of 110+ species including Royal Bengal " +
                    "Tiger, Asian Elephant, One-horned Rhinoceros, and various bird species. " +
                    "The zoo also has a snake house, aquarium, and a toy train for children. " +
                    "A perfect family outing spot with lush greenery and well-maintained enclosures.",
            nearestStation = "Raja Bazar",
            distance = "2 km from Raja Bazar station",
            timings = "9:00 AM – 5:00 PM (Closed on Mondays)",
            entryFee = "₹40 Adults | ₹20 Children",
            category = PlaceCategory.NATURE,
            highlights = listOf("153 Acres", "800+ Animals", "Toy Train", "Aquarium"),
            emoji = "🦁"
        ),
        PatnaPlace(
            id = "p3",
            name = "Golghar",
            nameHindi = "गोलघर",
            description = "Built in 1786 by Captain John Garstin for the British Army, Golghar is " +
                    "a massive beehive-shaped granary standing 29 meters high. It was built after " +
                    "the devastating famine of 1770 to store grain. Climb the 145 spiral steps to " +
                    "the top for a breathtaking 360° panoramic view of Patna city and the Ganges. " +
                    "It remains one of the most iconic landmarks of Patna.",
            nearestStation = "Gandhi Maidan",
            distance = "800m from Gandhi Maidan station",
            timings = "9:00 AM – 6:00 PM (Daily)",
            entryFee = "₹25 Adults | ₹15 Children",
            category = PlaceCategory.HERITAGE,
            highlights = listOf("Built 1786", "29m Tall", "360° View", "145 Steps"),
            emoji = "🏛️"
        ),
        PatnaPlace(
            id = "p4",
            name = "Patna Museum",
            nameHindi = "पटना संग्रहालय",
            description = "Established in 1917, Patna Museum (also known as Jadu Ghar) houses one of " +
                    "the rarest collections in India. Key exhibits include the Didarganj Yakshi " +
                    "(3rd century BC Mauryan polished sandstone sculpture), a 200-million-year-old " +
                    "fossilized tree, Chinese paintings, and Mughal era artifacts. The museum has " +
                    "an excellent gallery of Buddhist and Hindu sculptures from the Mauryan period.",
            nearestStation = "Gandhi Maidan",
            distance = "1 km from Gandhi Maidan station",
            timings = "10:30 AM – 4:30 PM (Closed on Mondays)",
            entryFee = "₹15 Indians | ₹250 Foreigners",
            category = PlaceCategory.MUSEUM,
            highlights = listOf("Didarganj Yakshi", "Fossil Tree", "Mauryan Art", "Since 1917"),
            emoji = "🏺"
        ),
        PatnaPlace(
            id = "p5",
            name = "Bihar Museum",
            nameHindi = "बिहार संग्रहालय",
            description = "Opened in 2015, Bihar Museum is a world-class modern museum spread over " +
                    "5.5 acres. It showcases Bihar's rich history from the Mauryan Empire to modern " +
                    "times through interactive galleries. Key sections include the Orientation " +
                    "Gallery, Children's Gallery, History Gallery, and the Didarganj Yakshi gallery. " +
                    "The architecture itself is award-winning, designed by Japanese firm Maki & Associates.",
            nearestStation = "Raja Bazar",
            distance = "1.5 km from Raja Bazar station",
            timings = "10:30 AM – 5:00 PM (Closed on Mondays)",
            entryFee = "₹50 Indians | ₹500 Foreigners",
            category = PlaceCategory.MUSEUM,
            highlights = listOf("World-Class", "Interactive", "Children's Gallery", "Japanese Design"),
            emoji = "🏛️"
        ),
        PatnaPlace(
            id = "p6",
            name = "Bapu Tower",
            nameHindi = "बापू टॉवर",
            description = "Standing tall in the heart of Gandhi Maidan, Bapu Tower is a memorial " +
                    "dedicated to Mahatma Gandhi. The tower offers a panoramic view of the " +
                    "surrounding area. Gandhi Maidan itself is a massive ground where historically " +
                    "significant rallies have been held — including by Mahatma Gandhi, " +
                    "Jayaprakash Narayan, and other freedom fighters.",
            nearestStation = "Gandhi Maidan",
            distance = "200m from Gandhi Maidan station",
            timings = "Open 24 hours (Park area)",
            entryFee = "Free",
            category = PlaceCategory.LANDMARK,
            highlights = listOf("Gandhi Memorial", "City View", "Historic Ground", "Central Location"),
            emoji = "🗼"
        ),
        PatnaPlace(
            id = "p7",
            name = "Sabhyata Dwar",
            nameHindi = "सभ्यता द्वार",
            description = "Sabhyata Dwar (Gateway of Civilization) is a grand entrance gate " +
                    "located at the Gandhi Maidan area. The architecture is inspired by the " +
                    "ancient Mauryan empire with a height of 63 feet, featuring intricate carvings " +
                    "depicting Bihar's cultural heritage. It serves as a tribute to Bihar's " +
                    "2,500-year-old civilizational history and is beautifully illuminated at night.",
            nearestStation = "Gandhi Maidan",
            distance = "400m from Gandhi Maidan station",
            timings = "Open 24 hours",
            entryFee = "Free",
            category = PlaceCategory.LANDMARK,
            highlights = listOf("63 Feet Tall", "Mauryan Design", "Night Lighting", "Cultural Symbol"),
            emoji = "🚪"
        ),
        PatnaPlace(
            id = "p8",
            name = "Buddha Smriti Park",
            nameHindi = "बुद्ध स्मृति पार्क",
            description = "Inaugurated by the Dalai Lama in 2010, Buddha Smriti Park is a serene " +
                    "22-acre park dedicated to Lord Buddha. It features the Patliputra Karuna " +
                    "Stupa housing Buddha relics, a meditation center, a museum, Bodhi Tree " +
                    "saplings, and a beautiful lotus pond. The park is an oasis of peace in the " +
                    "middle of the busy city, perfect for meditation and quiet reflection.",
            nearestStation = "Patna Junction",
            distance = "300m from Patna Junction station",
            timings = "10:00 AM – 5:00 PM (Closed on Mondays)",
            entryFee = "₹20 Adults | ₹10 Children",
            category = PlaceCategory.NATURE,
            highlights = listOf("Buddha Relics", "Meditation Center", "Lotus Pond", "22 Acres"),
            emoji = "🧘"
        ),
        PatnaPlace(
            id = "p9",
            name = "Patna Planetarium (Indira Gandhi Planetarium)",
            nameHindi = "पटना तारामंडल (इंदिरा गांधी तारामंडल)",
            description = "Built in the shape of the planet Saturn, Indira Gandhi Planetarium is " +
                    "one of the largest planetariums in Asia. It offers immersive astronomy shows " +
                    "in Hindi and English using advanced digital projection. The planetarium " +
                    "regularly hosts sky observation sessions and educational programs for " +
                    "students. A must-visit for science enthusiasts and families.",
            nearestStation = "Rajendra Nagar",
            distance = "1 km from Rajendra Nagar station",
            timings = "12:00 PM – 5:00 PM (Closed on Mondays & National Holidays)",
            entryFee = "₹30 Adults | ₹20 Children",
            category = PlaceCategory.LANDMARK,
            highlights = listOf("Saturn-Shaped", "Asia's Largest", "Astronomy Shows", "Digital Projection"),
            emoji = "🔭"
        ),
        PatnaPlace(
            id = "p10",
            name = "Agam Kuan",
            nameHindi = "अगम कुआँ",
            description = "Agam Kuan (Unfathomable Well) is an ancient well dating back to the " +
                    "Mauryan Empire (3rd century BC). It is believed to be one of the 'hell pits' " +
                    "built by Emperor Ashoka. The well is about 105 feet deep and has never dried up " +
                    "in over 2,300 years. It is protected by the Archaeological Survey of India " +
                    "and is a significant historical site of ancient Pataliputra.",
            nearestStation = "PMCH",
            distance = "1.5 km from PMCH station",
            timings = "6:00 AM – 6:00 PM (Daily)",
            entryFee = "Free",
            category = PlaceCategory.HERITAGE,
            highlights = listOf("2,300+ Years Old", "Mauryan Era", "105 Feet Deep", "ASI Protected"),
            emoji = "🕳️"
        )
    )

    fun getPlacesByCategory(category: PlaceCategory): List<PatnaPlace> =
        getAllPlaces().filter { it.category == category }

    fun searchPlaces(query: String): List<PatnaPlace> {
        if (query.isBlank()) return getAllPlaces()
        val q = query.lowercase().trim()
        return getAllPlaces().filter {
            it.name.lowercase().contains(q) ||
            it.nameHindi.contains(q) ||
            it.nearestStation.lowercase().contains(q) ||
            it.category.label.lowercase().contains(q)
        }
    }
}
