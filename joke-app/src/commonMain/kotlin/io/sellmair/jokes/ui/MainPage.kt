package io.sellmair.jokes.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainPage() = Box(
    Modifier.fillMaxSize(),
) {

    /* Place the joke */
    Row(
        Modifier.fillMaxWidth()
            .padding(12.dp)
            .align(Alignment.Center),
        horizontalArrangement = Arrangement.Center
    ) {
        JokeCard()
    }

}
