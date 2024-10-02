package io.sellmair.jokes

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.sellmair.evas.State
import io.sellmair.evas.collectEvents
import io.sellmair.evas.launchState
import kotlinx.coroutines.CoroutineScope

sealed class CurrentJokeState : State {
    data object Loading : CurrentJokeState()
    data class Error(val reason: String) : CurrentJokeState()
    data class Joke(val joke: String) : CurrentJokeState()

    companion object Key : State.Key<CurrentJokeState?> {
        override val default: CurrentJokeState? = null
    }
}

fun CoroutineScope.launchCurrentJokeState() = launchState { key: CurrentJokeState.Key ->
    CurrentJokeState.Loading.emit()

    suspend fun loadJoke() {
        val response = HttpClient().get("https://icanhazdadjoke.com/") {
            accept(ContentType.Text.Plain)
        }

        if (!response.status.isSuccess()) {
            CurrentJokeState.Error(response.status.description).emit()
            return
        }

        CurrentJokeState.Joke(response.bodyAsText()).emit()
    }

    loadJoke()
    collectEvents<ReloadJokeClickedEvent> {
        loadJoke()
    }

}