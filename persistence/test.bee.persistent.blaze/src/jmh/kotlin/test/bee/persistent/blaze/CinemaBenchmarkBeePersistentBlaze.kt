package test.bee.persistent.blaze

import common.benchmark.BasePersistenceBenchmark
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TestConfig::class])
@TestPropertySource("classpath:application-pg-bench.properties")
class CinemaBenchmarkBeePersistentBlaze : BasePersistenceBenchmark() {
    override val path: String = "../reports/bee.persistent.blaze"
    override val dataSize: DataSize = DataSize.LARGE
}