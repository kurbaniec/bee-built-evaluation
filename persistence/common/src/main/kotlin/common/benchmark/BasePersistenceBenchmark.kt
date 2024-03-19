package common.benchmark

import common.PersistenceTestSuite
import common.test.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.results.format.ResultFormatType
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.RunnerException
import org.openjdk.jmh.runner.options.Options
import org.openjdk.jmh.runner.options.OptionsBuilder
import org.openjdk.jmh.runner.options.TimeValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
@Suppress("unused")
@State(Scope.Benchmark)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
abstract class BasePersistenceBenchmark {

    enum class DataSize(val size: Int) {
        SMALL(500),
        LARGE(5000)
    }

    abstract val dataSize: DataSize

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    fun setTestSuite(testSuite: PersistenceTestSuite) {
        Companion.testSuite = testSuite
        setupBenchmark()
    }

    private fun setupBenchmark() {
        println("---setupBenchmark---")
        testSuite.insertData(dataSize.size)
        println("> data inserted")
    }

    companion object {
        lateinit var testSuite: PersistenceTestSuite

        // https://github.com/openjdk/jmh/blob/master/jmh-core/src/main/java/org/openjdk/jmh/runner/Defaults.java
        private const val MEASUREMENT_ITERATIONS = 5
        private const val WARMUP_ITERATIONS = 5
        private val WARMUP_TME = TimeValue.seconds(10)
        private val MEASUREMENT_TIME = TimeValue.seconds(10)

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

    abstract val path: String

    private fun createDir() {
        val file = File(path)
        file.mkdirs()
    }

    private fun filename(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")
        val formattedDateTime = currentDateTime.format(formatter)
        return "$path/$formattedDateTime.txt"
    }

    @Test
    @Throws(RunnerException::class)
    fun executeJmhRunner() {
        createDir()
        val filename = filename()
        val opt: Options =
            OptionsBuilder() // set the class name regex for benchmarks to search for to the current class
                .include(
                    "\\.(" + this.javaClass.simpleName + "|" +
                        BasePersistenceBenchmark::class.java.simpleName +
                        ")\\."
                )
                //.include("CinemaBenchmarkBeePersistentBlaze")
                .warmupIterations(WARMUP_ITERATIONS)
                .measurementIterations(MEASUREMENT_ITERATIONS) // do not use forking or the benchmark methods will not see references stored within its class
                .warmupTime(WARMUP_TME)
                .measurementTime(MEASUREMENT_TIME)
                .forks(0) // do not use multiple threads
                .threads(1)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.TEXT)
                .result(filename) // set this to a valid filename if you want reports
                .shouldFailOnError(true)
                .jvmArgs("-server")
                .build()

        Runner(opt).run()
    }

    @Benchmark
    fun b1EmptySelection(blackhole: Blackhole) {
        val cinemaBuffs = testSuite.performEmptySelection()
        blackhole.consume(cinemaBuffs)
    }

    @Benchmark
    fun b2PartialSelectionFavoritePopcornStand(blackhole: Blackhole) {
        val cinemaBuffs = testSuite.performPartialSelectionFavoritePopcornStand()
        blackhole.consume(cinemaBuffs)
    }

    @Benchmark
    fun b3PartialSelectionMovie(blackhole: Blackhole) {
        val cinemaBuffs = testSuite.performPartialSelectionMovie()
        blackhole.consume(cinemaBuffs)
    }

    @Benchmark
    fun b4PartialSelectionTickets(blackhole: Blackhole) {
        val cinemaBuffs = testSuite.performSelectionTickets()
        blackhole.consume(cinemaBuffs)
    }

    @Benchmark
    fun b5FullSelection(blackhole: Blackhole) {
        val cinemaBuffs = testSuite.performFullSelection()
        blackhole.consume(cinemaBuffs)
    }
}