@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package test.bee.persistent.blaze

import jakarta.persistence.EntityManager
import org.junit.jupiter.api.extension.ExtendWith
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import test.bee.persistent.blaze.BenchmarkUtil.Companion.testFromKotlin
import java.util.concurrent.TimeUnit


/**
 * Based on https://gist.github.com/msievers/ce80d343fc15c44bea6cbb741dde7e45.
 * Was not working: ERROR: Unable to find the resource: /META-INF/BenchmarkList
 * Seems that kapt is need so that the jmh annotation processor can correctly run
 * https://stackoverflow.com/questions/45085806/kotlins-kapt-plugin-for-gradle-does-not-work-for-custom-source-set-jmh
 * Also logging (println) seems to make test run endlessly…
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
        val value = em.metamodel.managedTypes
        println("benchmark $value")
    }

    @Benchmark
    fun benchmark(blackhole: Blackhole) {
        val result = testFromKotlin()
        blackhole.consume(result)
        val value = em.metamodel.managedTypes
        blackhole.consume(value)
    }
}