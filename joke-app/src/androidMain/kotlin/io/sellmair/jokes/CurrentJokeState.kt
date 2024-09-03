package io.sellmair.jokes

import io.sellmair.evas.State

sealed class CurrentJokeState : State {

    data object Loading : CurrentJokeState()
    data class Error(val message: String) : CurrentJokeState()
    data class Joke(val joke: String) : CurrentJokeState()

    companion object Key: State.Key<CurrentJokeState?> {
        override val default: CurrentJokeState? = null
    }
}