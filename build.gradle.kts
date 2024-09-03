plugins {
    kotlin("jvm")
    kotlin("plugin.compose")
    id("com.android.application") apply false
}

dependencies {
    implementation("org.jetbrains.compose.runtime:runtime:1.6.11")
}