package org.jetbrains.sample

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

actual fun platform(): String {
    return "Android"
}

actual fun scanBluetooth(): Flow<String> {
    return flow {
        while(currentCoroutineContext().isActive) {
            delay(Random.nextInt(3, 10).seconds)
            emit("Device: ${Random.nextBytes(4).toHexString()}")
        }
    }
}
