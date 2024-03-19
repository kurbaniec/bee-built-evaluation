package test.bee.persistent.blaze

import common.CinemaBuffBase
import common.PersistenceTestSuite
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import test.bee.persistent.blaze.dsl.CinemaBuffDSL

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-19
 */
@Component
class TestSuite(
    @Qualifier("testEM")
    val em: EntityManager,
    @Qualifier("testTM")
    transactionManager: PlatformTransactionManager,
    @Autowired
    val movieRepository: MovieRepository,
    @Autowired
    val cinemaHallRepository: CinemaHallRepository,
    @Autowired
    val ticketRepository: TicketRepository,
    @Autowired
    val popcornStandRepository: PopcornStandRepository,
    @Autowired
    val cinemaBuffRepository: CinemaBuffRepository,
) : PersistenceTestSuite {
    private val transaction = TransactionTemplate(transactionManager)
    override fun insertData(size: Int) {
        for (i in 1..size) {
            addCinema()
        }
    }

    private fun addCinema() {
        transaction.executeWithoutResult {
            var cinemaHallA = CinemaHall(hallName = "A", capacity = 100)
            cinemaHallA = cinemaHallRepository.persist(cinemaHallA)
            var cinemaHallB = CinemaHall(hallName = "B", capacity = 75)
            cinemaHallB = cinemaHallRepository.persist(cinemaHallB)
            var popcornStandA1 = PopcornStand(name = "Popcorn!", flavor = "Salty", price = 42.0, cinemaHallId = cinemaHallA.id)
            popcornStandA1 = popcornStandRepository.persist(popcornStandA1)
            var popcornStandA2 = PopcornStand(name = "Popcorn!!", flavor = "Salty", price = 42.0, cinemaHallId = cinemaHallA.id)
            popcornStandA2 = popcornStandRepository.persist(popcornStandA2)
            var popcornStandB1 = PopcornStand(name = "More Popcorn!", flavor = "Sweet", price = 42.0, cinemaHallId = cinemaHallB.id)
            popcornStandB1 = popcornStandRepository.persist(popcornStandB1)
            var popcornStandB2 = PopcornStand(name = "More Popcorn!!", flavor = "Sweet", price = 42.0, cinemaHallId = cinemaHallB.id)
            popcornStandB2 = popcornStandRepository.persist(popcornStandB2)
            var movieA = Movie(
                title = "Drive",
                director = "Nicolas Winding Refn",
                durationInMinutes = 100,
                genre = "Crime, Drama, Action",
                cinemaHallId = cinemaHallA.id
            )
            movieA = movieRepository.persist(movieA)
            var movieB = Movie(
                title = "Army of Darkness",
                director = "Sam Raimi",
                durationInMinutes = 81,
                genre = "Fantasy, Adventure, Horror, Comedy",
                cinemaHallId = cinemaHallB.id
            )
            movieB = movieRepository.persist(movieB)
            var cinemaBuff = CinemaBuff(
                name = "Max Cinema",
                favoriteGenre = "Comedy",
                favoritePopCornStandId = popcornStandA1.id
            )
            cinemaBuff = cinemaBuffRepository.persist(cinemaBuff)
            val ticketA = Ticket(price = 20.0, seatNumber = "D5", movieId = movieA.id, cinemaBuffId = cinemaBuff.id)
            ticketRepository.persist(ticketA)
            val ticketB = Ticket(price = 18.0, seatNumber = "C5", movieId = movieB.id, cinemaBuffId = cinemaBuff.id)
            ticketRepository.persist(ticketB)
        }
    }

    override fun performEmptySelection(): List<CinemaBuffBase>? {
        return transaction.execute {
            cinemaBuffRepository.select()
        }
    }

    override fun performPartialSelectionFavoritePopcornStand(): List<CinemaBuffBase>? {
        val selection = CinemaBuffDSL.select {
            this.favoritePopcornStand { }
        }
        return transaction.execute {
            cinemaBuffRepository.select(selection)
        }
    }

    override fun performPartialSelectionMovie(): List<CinemaBuffBase>? {
        val selection = CinemaBuffDSL.select {
            this.tickets {
                this.movie { }
            }
        }
        return transaction.execute {
            cinemaBuffRepository.select(selection)
        }
    }

    override fun performSelectionTickets(): List<CinemaBuffBase>? {
        val selection = CinemaBuffDSL.select {
            this.tickets {
                this.movie {
                    this.cinemaHall {
                        this.popcornStands {  }
                    }
                }
            }
        }
        return transaction.execute {
            cinemaBuffRepository.select(selection)
        }
    }

    override fun performFullSelection(): List<CinemaBuffBase>? {
        val selection = CinemaBuffDSL.select {
            this.favoritePopcornStand { }
            this.tickets {
                this.movie {
                    this.cinemaHall {
                        this.popcornStands {  }
                    }
                }
            }
        }
        return transaction.execute {
            cinemaBuffRepository.select(selection)
        }
    }
}