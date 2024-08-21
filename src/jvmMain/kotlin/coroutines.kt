import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.coroutines.*

fun main() {
    val coroutine = ::async.createCoroutine(Continuation(EmptyCoroutineContext) { result -> result.getOrThrow() })
    coroutine.resume(Unit)
}

suspend fun async() {
    println("Async")
}

interface Dispatcher : CoroutineContext.Element {
    fun dispatch(block: () -> Unit)

    override val key: CoroutineContext.Key<*> get() = Key

    companion object Key : CoroutineContext.Key<Dispatcher>
}

object MainDispatcher : Dispatcher {
    private val queue = LinkedBlockingQueue<() -> Unit>()

    override fun dispatch(block: () -> Unit) {
        queue.offer { block() }
    }

    fun loop() {
        while(true) {
            queue.poll(1, TimeUnit.SECONDS)?.invoke() ?: return
        }
    }

}