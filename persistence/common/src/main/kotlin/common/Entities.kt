package common

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-05
 */

interface MovieBase {
    val id: Long?
    val title: String
    val director: String
    val durationInMinutes: Int
    val genre: String
    val cinemaHall: CinemaHallBase?
}

interface CinemaHallBase {
    val id: Long?
    val hallName: String
    val capacity: Int
    val popcornStands: Collection<PopcornStandBase>?
}

interface TicketBase {
    val id: Long?
    val price: Double
    val seatNumber: String
    val movie: MovieBase?
    // val cinemaBuff: CinemaBuffBase?
}

interface PopcornStandBase {
    val id: Long?
    val name: String
    val flavor: String
    val price: Double
}

interface CinemaBuffBase {
    val id: Long?
    val name: String
    val favoriteGenre: String
    val tickets: Collection<TicketBase>?
    val favoritePopcornStand: PopcornStandBase?
}