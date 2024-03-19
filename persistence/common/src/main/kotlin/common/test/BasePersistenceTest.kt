package common.test

import common.PersistenceTestSuite
import common.PersistenceTestSuite.Companion.dataSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-19
 */

@Testcontainers
@TestMethodOrder(OrderAnnotation::class)
abstract class BasePersistenceTest : PersistenceTestSuite {

    @BeforeEach
    fun beforeEach() {
        println(performedSetup)
        if (performedSetup) return
        insertData(dataSize)
        performedSetup = true
    }

    companion object {
        var performedSetup = false

        @JvmStatic
        @Container
        val postgres = PostgreSQLContainer("postgres:16.2-alpine")

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource-test.jdbcUrl", postgres::getJdbcUrl)
            registry.add("spring.datasource-test.username", postgres::getUsername)
            registry.add("spring.datasource-test.password", postgres::getPassword)
        }
    }

    @Test
    @Order(1)
    fun `empty selection`() {
        val cinemaBuffs = performEmptySelection()
        assertEquals(dataSize, cinemaBuffs?.count())
        assertEmptySelection(cinemaBuffs)
    }

    @Test
    @Order(2)
    fun `partial selection - favoritePopcornStand`() {
        val cinemaBuffs = performPartialSelectionFavoritePopcornStand()
        assertEquals(dataSize, cinemaBuffs?.count())
        assertPartialSelectionFavoritePopcornStand(cinemaBuffs)
    }

    @Test
    @Order(3)
    fun `partial selection - movie`() {
        val cinemaBuffs = performPartialSelectionMovie()
        assertEquals(dataSize, cinemaBuffs?.count())
        assertPartialSelectionMovie(cinemaBuffs)
    }

    @Test
    @Order(4)
    fun `partial selection - tickets`() {
        val cinemaBuffs = performSelectionTickets()
        assertEquals(dataSize, cinemaBuffs?.count())
        assertPartialSelectionTickets(cinemaBuffs)
    }

    @Test
    @Order(5)
    fun `full selection`() {
        val cinemaBuffs = performFullSelection()
        assertEquals(dataSize, cinemaBuffs?.count())
        assertFullSelection(cinemaBuffs)
    }

}