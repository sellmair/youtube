package benchmark

import kotlinx.benchmark.*
import org.openjdk.jmh.annotations.OutputTimeUnit
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
open class ForEachBenchmark {
    private lateinit var list: MutableList<Int>

    @Setup
    fun setup() {
        list = mutableListOf()
        repeat(100_000) {
            list.add(it)
        }
    }

    @Benchmark
    fun forEach(blackhole: Blackhole) {
        list.forEach { blackhole.consume(it) }
    }

    @Benchmark
    fun forLoop(blackhole: Blackhole) {
        for(i in 0 until list.size) {
            blackhole.consume(list[i])
        }
    }
}