package io.sellmair.jokes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.sellmair.evas.State
import io.sellmair.evas.events
import io.sellmair.evas.launchState
import io.sellmair.jokes.network.httpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.conflate

sealed class _CurrentJokeState : State {
    companion object Key : State.Key<_CurrentJokeState?> {
        override val default: _CurrentJokeState? = null
    }

    data object Loading : _CurrentJokeState()
    data class Error(val message: String) : _CurrentJokeState()
    data class Joke(val joke: String) : _CurrentJokeState()
}

fun CoroutineScope.launchJokeLoadingState(): Job = launchState(_CurrentJokeState) {
    suspend fun loadJoke() {
        _CurrentJokeState.Loading.emit()

        val response = httpClient.get("https://icanhazdadjoke.com/") {
            accept(ContentType.Text.Plain)
        }

        if (!response.status.isSuccess()) {
            _CurrentJokeState.Error(response.status.description + ": ${response.bodyAsText()}").emit()
            return
        }

        _CurrentJokeState.Joke(response.bodyAsText()).emit()
    }

    /* Load the first initial joke immediately */
    loadJoke()

    /*
     Listen to 'Like' or 'Dislike' events and reload the joke;
     Conflated because we do not care about further like/dislike events until we finished loading
    */
    events<LikeDislikeEvent>().conflate().collect {
        loadJoke()
    }
}
