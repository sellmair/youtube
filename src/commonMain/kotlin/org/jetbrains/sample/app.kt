package org.jetbrains.sample

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun App() {
    val coroutineScope = rememberCoroutineScope()
    val bluetoothDevices = remember { mutableStateListOf<String>() }
    var scanning by remember { mutableStateOf<Job?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().animateContentSize(),
        horizontalAlignment = CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        Text("❄️ Chill Kotlin, it's cool 😎", fontSize = 24.0.sp)
        Text("Running on: ${platform()}")

        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            val currentScanning = scanning
            if (currentScanning != null) {
                currentScanning.cancel()
                scanning = null
                return@Button
            }
            scanning = coroutineScope.launch {
                scanBluetooth().collect { device ->
                    bluetoothDevices.add(device)
                }
            }
        }) {
            if (scanning != null) Text("Stop scanning")
            else Text("Scan for Bluetooth devices")

        }

        if (scanning != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Text("Scanning...")
            }
        }

        if (bluetoothDevices.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Devices Nearby", fontWeight = FontWeight.Bold)
            LazyColumn(modifier = Modifier.heightIn(min = 20.dp, max = 200.dp)) {
                items(bluetoothDevices) {
                    Text(it, fontWeight = FontWeight.Light)
                }
            }
        }
    }
}

expect fun platform(): String

expect fun scanBluetooth(): Flow<String>
