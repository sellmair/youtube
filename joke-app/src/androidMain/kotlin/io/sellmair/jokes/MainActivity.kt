package io.sellmair.jokes

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import io.sellmair.evas.*
import io.sellmair.evas.compose.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val states = States()
        val events = Events()

        lifecycleScope.launch(events + states) {
            launchCurrentJokeState()
        }

        setContent {
            installEvas(events, states) {
                Joke()
            }
        }
    }
}

data object ReloadJokeClickedEvent : Event

@Composable
fun Joke() {
    Button(onClick = EvasLaunching { ReloadJokeClickedEvent.emit() }) {
        when (val currentJokeState = CurrentJokeState.composeValue() ?: return@Button) {
            is CurrentJokeState.Error -> Text("Error: ${currentJokeState.reason}")
            is CurrentJokeState.Joke -> Text(currentJokeState.joke, fontSize = 24.sp)
            is CurrentJokeState.Loading -> CircularProgressIndicator()
        }
    }
}