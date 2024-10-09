import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp
import io.sellmair.evas.compose.EvasLaunching
import io.sellmair.evas.compose.composeValue
import io.sellmair.evas.set


@Composable
fun App() {
    val counter = CounterState.composeValue()
    Column {
        Row {
            Text("Button was pressed:", fontSize = 48.sp)
        }

        Row {
            Text("${counter.value} times", fontSize = 92.sp)
        }


        Row {
            Button(onClick = EvasLaunching {
                CounterState.set(CounterState(counter.value - 1))
            }) {
                Text("DEC", fontSize = 120.sp)
            }
        }
    }
}

