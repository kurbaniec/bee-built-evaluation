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
    testSuite: PersistenceTestSuite
) : BasePersistenceTest(testSuite)