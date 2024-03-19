package test.jpa.eager

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
class CinemaBenchmarkJpaEager : BasePersistenceBenchmark() {
    override val path: String = "../reports/jpa.eager"
    override val dataSize: DataSize = DataSize.LARGE
}