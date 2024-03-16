package common

import kotlin.test.assertFalse
import kotlin.test.assertNotNull

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-16
 */


data class AssertSelection(
    val favoritePopCornStand: Boolean = true,
    val tickets: Boolean = true,
    val movie: Boolean = true,
    val cinemaHall: Boolean = false,
    val popcornStands: Boolean = false
)

fun assertSelection(
    cinemaBuffs: List<CinemaBuffBase>?,
    selection: AssertSelection
) {
    assertFalse(cinemaBuffs.isNullOrEmpty())
    for (cb in cinemaBuffs)
        assertSelection(cb, selection)
}

fun assertSelection(
    cinemaBuff: CinemaBuffBase?,
    selection: AssertSelection
) {
    assertNotNull(cinemaBuff)
    if (selection.favoritePopCornStand) {
        val favoritePopcornStand = cinemaBuff.favoritePopcornStand
        assertNotNull(favoritePopcornStand)
    }
    if (!selection.tickets) return
    val tickets = cinemaBuff.tickets
    assertFalse(tickets.isNullOrEmpty())
    for (ticket in tickets) {
        if (!selection.movie) continue
        val movie = ticket.movie
        assertNotNull(movie)
        if (!selection.cinemaHall) continue
        val cinemaHall = movie.cinemaHall
        assertNotNull(cinemaHall)
        if (!selection.popcornStands) continue
        val popcornStands = cinemaHall.popcornStands
        assertFalse(popcornStands.isNullOrEmpty())
    }
}


fun assertEmptySelection(cinemaBuffBase: List<CinemaBuffBase>?) {
    val selection = AssertSelection(
        favoritePopCornStand = false,
        tickets = false,
        movie = false,
        cinemaHall = false,
        popcornStands = false
    )
    assertSelection(cinemaBuffBase, selection)
}


// val selection = CinemaBuffDSL.select {
//     this.favoritePopcornStand { }
//     this.tickets {
//         this.movie {
//             this.cinemaHall {
//                 this.popcornStand {  }
//             }
//         }
//     }
// }
fun assertFullSelection(cinemaBuffBase: List<CinemaBuffBase>?) {
    val selection = AssertSelection()
    assertSelection(cinemaBuffBase, selection)
}

// val selection = CinemaBuffDSL.select {
//     this.favoritePopcornStand { }
// }
fun assertPartialSelectionFavoritePopcornStand(
    cinemaBuffBase: List<CinemaBuffBase>?
) {
    val selection = AssertSelection(
        tickets = false
    )
    assertSelection(cinemaBuffBase, selection)
}

// val selection = CinemaBuffDSL.select {
//     this.tickets {
//         this.movie {
//             this.cinemaHall {
//                 this.popcornStand {  }
//             }
//         }
//     }
// }
fun assertPartialSelectionTickets(
    cinemaBuffBase: List<CinemaBuffBase>?
) {
    val selection = AssertSelection(
        favoritePopCornStand = false
    )
    assertSelection(cinemaBuffBase, selection)
}

// val selection = CinemaBuffDSL.select {
//     this.tickets {
//         this.movie {}
//     }
// }
fun assertPartialSelectionMovie(
    cinemaBuffBase: List<CinemaBuffBase>?
) {
    val selection = AssertSelection(
        favoritePopCornStand = false,
        cinemaHall = false
    )
    assertSelection(cinemaBuffBase, selection)
}