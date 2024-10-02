import kotlin.time.measureTime

fun main() {
    val list = mutableListOf<Int>()
    repeat(100_000) { list.add(it) }

    repeat(10000) {
        list.forEach { _ -> }
    }

    val forEachDuration = measureTime {
        list.forEach { _ -> }
    }
    println("forEach took: $forEachDuration")

    val alternativeDuration = measureTime {
        list.forEach { _ -> }
    }
    println("alternative took: $alternativeDuration")
}