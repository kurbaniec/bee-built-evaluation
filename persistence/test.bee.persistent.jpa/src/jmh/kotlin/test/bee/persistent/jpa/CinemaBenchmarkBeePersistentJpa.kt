package test.bee.persistent.jpa

import common.benchmark.BasePersistenceBenchmark
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-19
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TestConfig::class])
@TestPropertySource("classpath:application-pg-bench.properties")
class CinemaBenchmarkBeePersistentJpa : BasePersistenceBenchmark() {
    override val path: String = "../reports/bee.persistent.jpa"
    override val dataSize: Int = System.getProperty("dataSize").toInt()
}