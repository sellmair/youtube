package live

import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import kotlin.coroutines.*

fun main(args: Array<String>) {
    val coroutine = ::run.createCoroutine(args, Continuation(MainDispatcher) { result -> result.getOrThrow() })
    MainDispatcher.dispatch { coroutine.resume(Unit) }
    MainDispatcher.runLoop()
}

interface Dispatcher : CoroutineContext.Element {
    fun dispatch(block: () -> Unit)

    override val key: CoroutineContext.Key<*> get() = Key

    companion object Key : CoroutineContext.Key<Dispatcher>
}

object MainDispatcher : Dispatcher {
    private val queue = LinkedBlockingQueue<() -> Unit>()

    fun runLoop() {
        while (true) {
            queue.take().invoke()
        }
    }

    override fun dispatch(block: () -> Unit) {
        queue.offer { block() }
    }

}

object BackgroundDispatcher : Dispatcher {
    private val threads = Executors.newFixedThreadPool(4)
    override fun dispatch(block: () -> Unit) {
        threads.execute { block() }
    }
}

suspend fun run(args: Array<String>) {
    args.forEach { println(it) }
    println("Started in:" + Thread.currentThread().name)
    withContext(BackgroundDispatcher) {
        println("Continued in: " + Thread.currentThread().name)
        withContext(MainDispatcher) {
            println("Intercepted in: " + Thread.currentThread().name)
        }
    }
    println("Returned in: " + Thread.currentThread().name)
}

suspend fun <T> withContext(context: CoroutineContext, action: suspend () -> T): T {
    return suspendCoroutine { outerContinuation ->
        val newContext = outerContinuation.context + context

        val newCoroutine = action.createCoroutine(Continuation(newContext) { result ->
            val dispatcher = outerContinuation.context[Dispatcher] ?: error("No dispatcher found")
            dispatcher.dispatch {
                outerContinuation.resumeWith(result)
            }
        })

        val dispatcher = newContext[Dispatcher] ?: error("No dispatcher found")
        dispatcher.dispatch {
            newCoroutine.resume(Unit)
        }
    }
}