package org.jetbrains.sample

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.isActive
import kotlinx.coroutines.isActive
import platform.posix.send
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

actual fun platform(): String {
    return "iOS"
}

actual fun scanBluetooth(): Flow<String> {
    return flow {
        while(currentCoroutineContext().isActive) {
            delay(Random.nextInt(3, 10).seconds)
            emit("Device: ${Random.nextBytes(4).toHexString()}")
        }
    }
}
