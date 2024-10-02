package io.sellmair.jokes

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import io.sellmair.evas.Events
import io.sellmair.evas.States
import io.sellmair.evas.compose.*
import kotlinx.coroutines.launch

/*
class __MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val events = Events()
        val states = States()

        lifecycleScope.launch(events + states) {
            launchCurrentJokeState()
        }

        setContent {
            installEvents(events) {
                installStates(states) {
                    JokeCard()
                }
            }
        }
    }
}

@Composable
fun JokeCard() {
    Log.i("Track-C", "JokeCard")
    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(12.dp)
            .animateContentSize()
            .fillMaxWidth()
    ) {

        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val currentJoke = _CurrentJokeState.composeValue()) {
                is io.sellmair.jokes.CurrentJokeState._CurrentJokeState.Error -> Text("Error: ${currentJoke.message}")
                is io.sellmair.jokes.CurrentJokeState._CurrentJokeState.Loading -> CircularProgressIndicator()
                is io.sellmair.jokes.CurrentJokeState._CurrentJokeState.Joke -> Text(currentJoke.joke)
                null -> Unit
            }
        }
    }

}

 */