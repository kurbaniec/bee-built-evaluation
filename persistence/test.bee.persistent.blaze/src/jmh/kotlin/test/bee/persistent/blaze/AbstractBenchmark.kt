
package test.bee.persistent.blaze

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.results.format.ResultFormatType
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.RunnerException
import org.openjdk.jmh.runner.options.Options
import org.openjdk.jmh.runner.options.OptionsBuilder
import org.openjdk.jmh.runner.options.TimeValue
import java.io.File
import kotlin.test.Test



/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-16
 */


abstract class AbstractBenchmark {

    companion object {
        private const val MEASUREMENT_ITERATIONS = 3
        private const val WARMUP_ITERATIONS = 3
    }

    @Test
    @Throws(RunnerException::class)
    fun executeJmhRunner() {
        val file = File("./build/baum")
        file.mkdirs()

        val opt: Options =
            OptionsBuilder() // set the class name regex for benchmarks to search for to the current class
                .include("\\." + this.javaClass.simpleName + "\\.")
                //.include("CinemaBenchmarkBeePersistentBlaze")
                .warmupIterations(WARMUP_ITERATIONS)
                .measurementIterations(MEASUREMENT_ITERATIONS) // do not use forking or the benchmark methods will not see references stored within its class
                .warmupTime(TimeValue.milliseconds(200))
                .measurementTime(TimeValue.milliseconds(200))
                .forks(0) // do not use multiple threads
                .threads(1)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON)
                .result("./build/baum/result.json") // set this to a valid filename if you want reports
                .shouldFailOnError(true)
                .jvmArgs("-server")
                .build()

        Runner(opt).run()
    }

    // @Benchmark
    // open fun someBenchmark(blackhole: Blackhole) {
    //     val result = BenchmarkUtil.testFromKotlin()
    //     blackhole.consume(result)
    //     val value = CinemaBenchmarkBeePersistentBlaze.em.metamodel.managedTypes
    //     blackhole.consume(value)
    // }

}
