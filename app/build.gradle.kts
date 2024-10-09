import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform") version "2.0.20"
    kotlin("plugin.compose") version "2.0.20"
    id("org.jetbrains.compose") version "1.6.11"
}

kotlin {
    jvmToolchain(17)
    jvm()

    sourceSets.jvmMain.dependencies {
        implementation("io.sellmair:evas:1.0.0")
        implementation("io.sellmair:evas-compose:1.0.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
        implementation(compose.desktop.currentOs)
        implementation(compose.foundation)
        implementation(compose.material3)

        implementation(project(":app-core"))
    }
}
