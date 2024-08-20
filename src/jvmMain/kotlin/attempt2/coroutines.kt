package attempt2

import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.coroutines.*

private val mainQueue = LinkedBlockingQueue<() -> Unit>()
private var isActive = true


fun main(args: Array<String>) {
    val continuation = Continuation(MainDispatcher) { result ->
        result.getOrThrow()
    }
    val coroutine = ::run.createCoroutine(args, continuation)
    mainQueue.offer { coroutine.resume(Unit) }

    while (true) {
        mainQueue.take().invoke()
    }
}

suspend fun run(args: Array<String>) {
    println("Hello from my first coroutine! (${Thread.currentThread().name})")
    args.forEach { println("Arg: $it") }

    withBackgroundThread {
        println("Called from ${Thread.currentThread().name}")
        withBackgroundThread {
            println("Called from ${Thread.currentThread().name}")
        }
    }

    println("Returned to ${Thread.currentThread().name}")
}


object MainDispatcher : Dispatcher {

    override fun dispatch(block: () -> Unit) {
        mainQueue.offer { block() }
    }
}

object BackgroundDispatcher : Dispatcher {
    private val nextThreadId = AtomicInteger(0)
    private val backgroundThreads = Executors.newFixedThreadPool(4) { runnable ->
        thread(
            start = false,
            isDaemon = true,
            name = "background (${nextThreadId.getAndIncrement()})",
            block = runnable::run
        )
    }

    override fun dispatch(block: () -> Unit) {
        backgroundThreads.execute { block() }
    }
}

interface Dispatcher : CoroutineContext.Element {

    fun dispatch(block: () -> Unit)
    override val key: CoroutineContext.Key<Dispatcher> get() = Key

    companion object Key : CoroutineContext.Key<Dispatcher>
}

suspend fun <T> withBackgroundThread(block: suspend () -> T): T {
    return withContext(BackgroundDispatcher) { block() }
}

suspend fun <T> withContext(context: CoroutineContext, action: suspend () -> T): T {

    return suspendCoroutine<T> { outerContinuation ->
        val newContext = outerContinuation.context + context

        val newCoroutine = action.createCoroutine(Continuation(newContext) { result ->
            val dispatcher = outerContinuation.context[Dispatcher] ?: return@Continuation
            dispatcher.dispatch {
                outerContinuation.resumeWith(result)
            }
        })

        val dispatcher = newContext[Dispatcher] ?: error("Missing Dispatcher")
        dispatcher.dispatch {
            newCoroutine.resume(Unit)
        }
    }
}