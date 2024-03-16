@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package test.bee.persistent.blaze

import jakarta.persistence.EntityManager
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.openjdk.jmh.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.concurrent.TimeUnit


/**
 * Based on https://gist.github.com/msievers/ce80d343fc15c44bea6cbb741dde7e45.
 * https://stackoverflow.com/questions/45085806/kotlins-kapt-plugin-for-gradle-does-not-work-for-custom-source-set-jmh
 *
 * @author Kacper Urbaniec
 * @version 2024-03-16
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TestConfig::class])
@TestPropertySource("classpath:application.properties")
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CinemaBenchmarkBeePersistentBlaze : AbstractBenchmark() {

    companion object {
        lateinit var em: EntityManager
    }

    @Autowired
    fun setDslContext(@Qualifier("testEM") em: EntityManager) {
        Companion.em = em
    }

    @Setup(Level.Trial)
    fun setupBenchmark() {
        println("setup")
    }

    @Benchmark
    fun benchmark() {
        val value = em.metamodel.managedTypes
        println("benchmark $value")
    }
}