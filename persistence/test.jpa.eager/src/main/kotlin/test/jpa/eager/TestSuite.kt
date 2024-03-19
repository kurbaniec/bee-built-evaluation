package test.jpa.eager

import com.beeproduced.bee.persistent.jpa.proxy.Unproxy
import common.CinemaBuffBase
import common.PersistenceTestSuite
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

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
            cinemaHallA = cinemaHallRepository.save(cinemaHallA)
            var cinemaHallB = CinemaHall(hallName = "B", capacity = 75)
            cinemaHallB = cinemaHallRepository.save(cinemaHallB)
            var popcornStandA1 = PopcornStand(name = "Popcorn!", flavor = "Salty", price = 42.0, cinemaHallId = cinemaHallA.id)
            popcornStandA1 = popcornStandRepository.save(popcornStandA1)
            var popcornStandA2 = PopcornStand(name = "Popcorn!!", flavor = "Salty", price = 42.0, cinemaHallId = cinemaHallA.id)
            popcornStandA2 = popcornStandRepository.save(popcornStandA2)
            var popcornStandB1 = PopcornStand(name = "More Popcorn!", flavor = "Sweet", price = 42.0, cinemaHallId = cinemaHallB.id)
            popcornStandB1 = popcornStandRepository.save(popcornStandB1)
            var popcornStandB2 = PopcornStand(name = "More Popcorn!!", flavor = "Sweet", price = 42.0, cinemaHallId = cinemaHallB.id)
            popcornStandB2 = popcornStandRepository.save(popcornStandB2)
            var movieA = Movie(
                title = "Drive",
                director = "Nicolas Winding Refn",
                durationInMinutes = 100,
                genre = "Crime, Drama, Action",
                cinemaHallId = cinemaHallA.id
            )
            movieA = movieRepository.save(movieA)
            var movieB = Movie(
                title = "Army of Darkness",
                director = "Sam Raimi",
                durationInMinutes = 81,
                genre = "Fantasy, Adventure, Horror, Comedy",
                cinemaHallId = cinemaHallB.id
            )
            movieB = movieRepository.save(movieB)
            var cinemaBuff = CinemaBuff(
                name = "Max Cinema",
                favoriteGenre = "Comedy",
                favoritePopCornStandId = popcornStandA1.id
            )
            cinemaBuff = cinemaBuffRepository.save(cinemaBuff)
            val ticketA = Ticket(price = 20.0, seatNumber = "D5", movieId = movieA.id, cinemaBuffId = cinemaBuff.id)
            ticketRepository.save(ticketA)
            val ticketB = Ticket(price = 18.0, seatNumber = "C5", movieId = movieB.id, cinemaBuffId = cinemaBuff.id)
            ticketRepository.save(ticketB)
        }
    }

    override fun performEmptySelection(): List<CinemaBuffBase>? {
        return transaction.execute {
            cinemaBuffRepository
                .findAll()
                .also { Unproxy.unproxyEntities(it) }
        }
    }

    override fun performPartialSelectionFavoritePopcornStand(): List<CinemaBuffBase>? {
        return transaction.execute {
            cinemaBuffRepository
                .findAll()
                .also { Unproxy.unproxyEntities(it) }
        }
    }

    override fun performPartialSelectionMovie(): List<CinemaBuffBase>? {
        return transaction.execute {
            cinemaBuffRepository
                .findAll()
                .also { Unproxy.unproxyEntities(it) }
        }
    }

    override fun performSelectionTickets(): List<CinemaBuffBase>? {
        return transaction.execute {
            cinemaBuffRepository
                .findAll()
                .also { Unproxy.unproxyEntities(it) }
        }
    }

    override fun performFullSelection(): List<CinemaBuffBase>? {
        return transaction.execute {
            cinemaBuffRepository
                .findAll()
                .also { Unproxy.unproxyEntities(it) }
        }
    }
}