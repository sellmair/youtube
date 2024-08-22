import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.coroutines.*

fun main() {
    val coroutine = ::async.createCoroutine(Continuation(MainDispatcher) { result -> result.getOrThrow() })
    MainDispatcher.dispatch { coroutine.resume(Unit) }
    MainDispatcher.loop()
}

suspend fun async() {
    println("Async: ${Thread.currentThread().name}")
    withContext(BackgroundDispatcher) {
        println("Changed to ${Thread.currentThread().name}")
        withContext(MainDispatcher) {
            println("Changed to ${Thread.currentThread().name}")
        }
    }
    println("Done in ${Thread.currentThread().name}")
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
        while (true) {
            queue.poll(1, TimeUnit.SECONDS)?.invoke() ?: return
        }
    }
}

object BackgroundDispatcher : Dispatcher {
    private val executors = Executors.newFixedThreadPool(4) { runnable ->
        thread(start = false, isDaemon = true, block = runnable::run)
    }

    override fun dispatch(block: () -> Unit) {
        executors.execute { block() }
    }
}

suspend fun<T> withContext(context: CoroutineContext, action: suspend() -> T): T {
    return suspendCoroutine { outerContinuation ->
        val newContext = outerContinuation.context + context
        val newCoroutine = action.createCoroutine(Continuation(newContext) { result ->
            val dispatcher = outerContinuation.context[Dispatcher]?: error("No dispatcher found")
            dispatcher.dispatch {
                outerContinuation.resumeWith(result)
            }
        })

        val newDispatcher = newContext[Dispatcher]?: error("No dispatcher found")
        newDispatcher.dispatch {
            newCoroutine.resume(Unit)
        }
    }
}