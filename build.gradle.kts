plugins {
    kotlin("multiplatform") version "2.2.0-RC"
    kotlin("plugin.compose") version "2.2.0-RC"
    id("com.android.application") version "8.7.3"

    id("org.jetbrains.compose") version "1.8.0"
    id("org.jetbrains.compose.hot-reload") version "1.0.0-alpha10"
}

android {
    compileSdk = 35
    namespace = "or.jetbrains.sample"
    defaultConfig {
        minSdk = 32
    }
}

kotlin {
    androidTarget()
    iosArm64()
    iosSimulatorArm64()

    sourceSets.commonMain.dependencies {
        implementation(compose.ui)
        implementation(compose.foundation)
        implementation(compose.material3)
    }

    sourceSets.androidMain.dependencies {
        implementation("androidx.activity:activity-ktx:1.10.1")
        implementation("androidx.activity:activity-compose:1.10.1")
    }
}
