package org.jetbrains.sample

import androidx.compose.ui.window.singleWindowApplication

fun main() {
    singleWindowApplication(alwaysOnTop = true) {
        App()
    }
}
