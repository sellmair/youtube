package io.sellmair.jokes

import androidx.compose.runtime.Stable
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.sellmair.evas.State
import io.sellmair.evas.launchState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/*
@Stable
sealed class _CurrentJokeState : State {

    data object Loading : _CurrentJokeState()
    data class Error(val message: String) : _CurrentJokeState()
    data class Joke(val joke: String) : _CurrentJokeState()

    companion object Key : State.Key<_CurrentJokeState?> {
        override val default: _CurrentJokeState? = null
    }
}

fun CoroutineScope.launchCurrentJokeState() = launchState(
    Dispatchers.Default,
) { _: io.sellmair.jokes._CurrentJokeState.Key ->
    _CurrentJokeState.Loading.emit()

    val response = HttpClient().get("https://icanhazdadjoke.com/") {
        accept(ContentType.Text.Plain)
    }

    if (!response.status.isSuccess()) {
        _CurrentJokeState.Error(response.status.description).emit()
        return@launchState
    }

    _CurrentJokeState.Joke(response.bodyAsText()).emit()
}
*
 */