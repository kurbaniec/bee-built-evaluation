package test.jpa.lazy

import org.hibernate.Hibernate

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-16
 */

// val selection = SimpleSelection(setOf(
//     SimpleSelection.FieldNode(CinemaBuff::favoritePopcornStand.name),
//     SimpleSelection.FieldNode(
//         CinemaBuff::tickets.name, setOf(
//             SimpleSelection.FieldNode(
//                 Ticket::movie.name, setOf(
//                     SimpleSelection.FieldNode(
//                         Movie::cinemaHall.name, setOf(
//                             SimpleSelection.FieldNode(CinemaHall::popcornStands.name)
//                         ))
//                 ))
//         ))
// ))
fun loadFullSelection(cinemaBuffs: List<CinemaBuff>): List<CinemaBuff> {
    for (cb in cinemaBuffs)
        loadFullSelection(cb)
    return cinemaBuffs
}

fun loadFullSelection(cinemaBuff: CinemaBuff) {
    Hibernate.initialize(cinemaBuff.favoritePopcornStand)
    Hibernate.initialize(cinemaBuff.tickets)
    val tickets = cinemaBuff.tickets!!
    for (ticket in tickets) {
        Hibernate.initialize(ticket.movie)
        val movie = ticket.movie!!
        Hibernate.initialize(movie.cinemaHall)
        val cinemaHall = movie.cinemaHall!!
        Hibernate.initialize(cinemaHall.popcornStands)
        val popcornStands = cinemaHall.popcornStands!!
        for (popcornStand in popcornStands)
            Hibernate.initialize(popcornStand)
    }
}

// val selection = SimpleSelection(setOf(
//     SimpleSelection.FieldNode(CinemaBuff::favoritePopcornStand.name),
// ))
fun loadPartialSelectionFavoritePopcornStand(cinemaBuffs: List<CinemaBuff>): List<CinemaBuff> {
    for (cb in cinemaBuffs)
        loadPartialSelectionFavoritePopcornStand(cb)
    return cinemaBuffs
}

fun loadPartialSelectionFavoritePopcornStand(cinemaBuff: CinemaBuff) {
    Hibernate.initialize(cinemaBuff.favoritePopcornStand)
}

// val selection = SimpleSelection(setOf(
//     SimpleSelection.FieldNode(
//         CinemaBuff::tickets.name, setOf(
//             SimpleSelection.FieldNode(
//                 Ticket::movie.name, setOf(
//                     SimpleSelection.FieldNode(
//                         Movie::cinemaHall.name, setOf(
//                             SimpleSelection.FieldNode(CinemaHall::popcornStands.name)
//                         )
//                     )
//                 )
//             )
//         )
//     )
// ))
fun loadPartialSelectionTickets(cinemaBuffs: List<CinemaBuff>): List<CinemaBuff> {
    for (cb in cinemaBuffs)
        loadPartialSelectionTickets(cb)
    return cinemaBuffs
}

fun loadPartialSelectionTickets(cinemaBuff: CinemaBuff) {
    Hibernate.initialize(cinemaBuff.tickets)
    val tickets = cinemaBuff.tickets!!
    for (ticket in tickets) {
        Hibernate.initialize(ticket.movie)
        val movie = ticket.movie!!
        Hibernate.initialize(movie.cinemaHall)
        val cinemaHall = movie.cinemaHall!!
        Hibernate.initialize(cinemaHall.popcornStands)
        val popcornStands = cinemaHall.popcornStands!!
        for (popcornStand in popcornStands)
            Hibernate.initialize(popcornStand)
    }
}

// val selection = SimpleSelection(setOf(
//     SimpleSelection.FieldNode(
//         CinemaBuff::tickets.name, setOf(
//             SimpleSelection.FieldNode(Ticket::movie.name)
//         )
//     )
// ))
fun loadPartialSelectionMovie(cinemaBuffs: List<CinemaBuff>): List<CinemaBuff> {
    for (cb in cinemaBuffs)
        loadPartialSelectionMovie(cb)
    return cinemaBuffs
}

fun loadPartialSelectionMovie(cinemaBuff: CinemaBuff) {
    Hibernate.initialize(cinemaBuff.tickets)
    val tickets = cinemaBuff.tickets!!
    for (ticket in tickets) {
        Hibernate.initialize(ticket.movie)
    }
}

