package test.bee.persistent.blaze

import common.CinemaBuffBase
import common.PersistenceTestSuite
import common.test.BasePersistenceTest
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-05
 */

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TestConfig::class])
@TestPropertySource("classpath:application-pg.properties")
class CinemaTestBeePersistentBlaze(
    @Autowired
    val testSuite: PersistenceTestSuite
) : BasePersistenceTest() {
    override fun insertData(size: Int) {
        testSuite.insertData(size)
    }

    override fun performEmptySelection(): List<CinemaBuffBase>? {
        return testSuite.performEmptySelection()
    }

    override fun performPartialSelectionFavoritePopcornStand(): List<CinemaBuffBase>? {
        return testSuite.performPartialSelectionFavoritePopcornStand()
    }

    override fun performPartialSelectionMovie(): List<CinemaBuffBase>? {
        return testSuite.performPartialSelectionMovie()
    }

    override fun performSelectionTickets(): List<CinemaBuffBase>? {
        return testSuite.performSelectionTickets()
    }

    override fun performFullSelection(): List<CinemaBuffBase>? {
        return testSuite.performFullSelection()
    }

}