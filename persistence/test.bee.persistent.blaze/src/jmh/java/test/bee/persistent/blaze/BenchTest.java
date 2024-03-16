package test.bee.persistent.blaze;

import org.junit.jupiter.api.extension.ExtendWith;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

/**
 * @author Kacper Urbaniec
 * @version 2024-03-16
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfig.class})
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class BenchTest extends AbstractBenchmarBase {

    @Setup(Level.Trial)
    public void setupBenchmark() {
        System.out.println("SETUP!");
    }

    @Benchmark
    public void someBenchmarkMethod(Blackhole blackhole) {
        var result = BenchmarkUtil.testFromKotlin();
        blackhole.consume(result);
    }
}
