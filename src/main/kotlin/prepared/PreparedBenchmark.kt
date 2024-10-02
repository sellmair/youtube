package prepared

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.BenchmarkTimeUnit
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Measurement
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlinx.benchmark.Warmup

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 500, timeUnit = BenchmarkTimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@State(Scope.Benchmark)
open class PreparedBenchmark {
    val list = mutableListOf<Int>()

    @Setup
    fun setup() {
        repeat(100_000) { list.add(it) }
    }

    @Benchmark
    fun forEach(blackhole: Blackhole) {
        list.forEach { blackhole.consume(it) }
    }

    @Benchmark
    fun forLoop(blackhole: Blackhole) {
        for (i in 0 until list.size) {
            blackhole.consume(list[i])
        }
    }
}

