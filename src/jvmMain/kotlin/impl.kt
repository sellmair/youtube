import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private val backgroundThreads = Executors.newFixedThreadPool(4)

fun stop() {
    backgroundThreads.shutdown()
}

fun <T> inBackgroundThread(block: () -> T): Future<T> {
    val future = FutureImpl<T>()
    backgroundThreads.execute {
        try {
            future.complete(block())
        } catch (t: Throwable) {
            future.completeWithError(t)
        }
    }

    return future
}

internal const val ansiReset = "\u001B[0m"
internal const val ansiCyan = "\u001B[36m"
internal const val ansiGreen = "\u001B[32m"

fun green(value: String) = "$ansiGreen$value$ansiReset"
fun cyan(value: String) = "$ansiCyan$value$ansiReset"


class FutureImpl<T> : Future<T> {
    private val lock = ReentrantLock()

    private var completed = false
    private var result: T? = null
    private var error: Any? = null

    private val successListeners = mutableListOf<(T) -> Unit>()
    private val failureListeners = mutableListOf<(Any) -> Unit>()

    fun complete(value: T) {
        lock.withLock {
            completed = true
            result = value
            successListeners.toList().also {
                successListeners.clear()
                failureListeners.clear()
            }
        }.forEach { listener -> listener.invoke(value) }
    }

    fun completeWithError(error: Any) {
        lock.withLock {
            this.completed = true
            this.error = error
            failureListeners.toList().also {
                successListeners.clear()
                failureListeners.clear()
            }
        }.forEach { listener -> listener.invoke(error) }
    }

    override fun onSuccess(listener: (value: T) -> Unit) = apply {
        val result = lock.withLock {
            if (!completed) {
                successListeners.add(listener)
                return@apply
            }
            if (error != null) {
                return@apply
            }
            result
        }

        @Suppress("UNCHECKED_CAST")
        listener.invoke(result as T)
    }

    override fun onError(listener: (error: Any) -> Unit) = apply {
        val error = lock.withLock {
            if (!completed) {
                failureListeners.add(listener)
                return@apply
            }
            error
        }

        if (error != null) {
            listener(error)
        }
    }
}
