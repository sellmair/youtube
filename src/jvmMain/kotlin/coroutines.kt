import java.net.URL
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.writeText

interface Future<T> {
    fun onSuccess(listener: (value: T) -> Unit): Future<T>
    fun onError(listener: (error: Any) -> Unit): Future<T>
}

fun Future<*>.onComplete(listener: () -> Unit) {
    onSuccess { listener() }
    onError { listener() }
}

fun downloadJokeAsync(): Future<String> {
    return inBackgroundThread {
        URL("https://v2.jokeapi.dev/joke/Any?format=txt").openStream().use { stream ->
            stream.readAllBytes().decodeToString()
        }
    }
}

fun getUserInputAsync(): Future<String> {
    return inBackgroundThread {
        readlnOrNull() ?: error("Failed to get user input")
    }
}

fun writeFileAsync(path: Path, text: String): Future<Unit> {
    return inBackgroundThread {
        path.writeText(text)
    }
}

fun main() {
    classicMain()
}

fun classicMain() {
    downloadJokeAsync().onSuccess { joke ->
        println(green(joke))
        println()
        println("Do you want to store the joke? (Y/n)")
        getUserInputAsync().onSuccess { answer ->
            if (answer == "Y" || answer == "y") {
                writeFileAsync(Path("joke.txt"), joke).onComplete {
                    stop()
                }
            }
        }.onError {
            stop()
        }
    }
}