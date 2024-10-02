package prepared
import kotlin.time.measureTime

fun main() {
    val list = mutableListOf<Int>()
    repeat(100_000) { list.add(it) }

    val forEachDuration = measureTime {
        list.forEach { _ -> }
    }
    println("forEach took $forEachDuration")


    val forLoopDuration = measureTime {
        list.fastForEach { _ -> }
    }
    println("forLoop took $forLoopDuration")
}

inline fun <T> List<T>.fastForEach(action: (T) -> Unit) {
    for (element in this) {
        action(element)
    }
}