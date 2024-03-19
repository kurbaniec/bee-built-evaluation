package test.bee.persistent.jpa

import com.beeproduced.bee.persistent.selection.SimpleSelection
import com.beeproduced.bee.persistent.selection.SimpleSelection.FieldNode
import common.test.*
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import kotlin.test.Test

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-05
 */

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TestConfig::class])
@TestPropertySource("classpath:application.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CinemaTestBeePersistentJpa(
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
) {
    private val transaction = TransactionTemplate(transactionManager)

    @BeforeAll
    fun beforeAll() = clear()

    @AfterEach
    fun afterEach() = clear()

    fun clear() = transaction.executeWithoutResult {
        ticketRepository.deleteAll()
        cinemaBuffRepository.deleteAll()
        movieRepository.deleteAll()
        popcornStandRepository.deleteAll()
        cinemaHallRepository.deleteAll()
    }

    @Test
    fun `empty selection`() {
        addCinemas()
        val cinemaBuffs = transaction.execute {
            cinemaBuffRepository.select()
        }
        assertEmptySelection(cinemaBuffs)
    }

    @Test
    fun `full selection`() {
        addCinemas()
        val selection = SimpleSelection(setOf(
            FieldNode(CinemaBuff::favoritePopcornStand.name),
            FieldNode(CinemaBuff::tickets.name, setOf(
                FieldNode(Ticket::movie.name, setOf(
                    FieldNode(Movie::cinemaHall.name, setOf(
                        FieldNode(CinemaHall::popcornStands.name)
                    ))
                ))
            ))
        ))
        val cinemaBuffs = transaction.execute {
            cinemaBuffRepository.select(selection)
        }
        assertFullSelection(cinemaBuffs)
    }

    @Test
    fun `partial selection - favoritePopcornStand`() {
        addCinemas()
        val selection = SimpleSelection(setOf(
            FieldNode(CinemaBuff::favoritePopcornStand.name),
        ))
        val cinemaBuffs = transaction.execute {
            cinemaBuffRepository.select(selection)
        }
        assertPartialSelectionFavoritePopcornStand(cinemaBuffs)
    }

    @Test
    fun `partial selection - tickets`() {
        addCinemas()
        val selection = SimpleSelection(setOf(
            FieldNode(CinemaBuff::tickets.name, setOf(
                FieldNode(Ticket::movie.name, setOf(
                    FieldNode(Movie::cinemaHall.name, setOf(
                        FieldNode(CinemaHall::popcornStands.name)
                    ))
                ))
            ))
        ))
        val cinemaBuffs = transaction.execute {
            cinemaBuffRepository.select(selection)
        }
        assertPartialSelectionTickets(cinemaBuffs)
    }

    @Test
    fun `partial selection - movie`() {
        addCinemas()
        val selection = SimpleSelection(setOf(
            FieldNode(CinemaBuff::tickets.name, setOf(
                FieldNode(Ticket::movie.name)
            ))
        ))
        val cinemaBuffs = transaction.execute {
            cinemaBuffRepository.select(selection)
        }
        assertPartialSelectionMovie(cinemaBuffs)
    }


    private fun addCinemas() {
        for (i in 1..5) {
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
}