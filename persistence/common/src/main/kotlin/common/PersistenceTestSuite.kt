package common

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-19
 */
interface PersistenceTestSuite {

    companion object {
        var dataSize = 5
    }

    fun insertData(size: Int)

    fun performEmptySelection(): List<CinemaBuffBase>?
    fun performPartialSelectionFavoritePopcornStand(): List<CinemaBuffBase>?
    fun performPartialSelectionMovie(): List<CinemaBuffBase>?
    fun performSelectionTickets(): List<CinemaBuffBase>?
    fun performFullSelection(): List<CinemaBuffBase>?
}