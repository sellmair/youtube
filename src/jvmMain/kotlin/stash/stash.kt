package stash

import java.util.concurrent.LinkedBlockingQueue
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume

fun main(args: Array<String>) {
    val queue = LinkedBlockingQueue<() -> Unit>()
    val coroutine = ::run.createCoroutine(args, Continuation(EmptyCoroutineContext) { result -> result.getOrThrow() })
    queue.offer { coroutine.resume(Unit) }


    while (true) {
        queue.take().invoke()
    }
}

suspend fun run(args: Array<String>) {
    args.forEach { println(it) }
}