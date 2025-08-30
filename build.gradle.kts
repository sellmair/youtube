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

    jvm()

    sourceSets.commonMain.dependencies {
        implementation(compose.ui)
        implementation(compose.foundation)
        implementation(compose.material3)
    }

    sourceSets.commonTest.dependencies {
        implementation(kotlin("test"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    }

    sourceSets.androidMain.dependencies {
        implementation("androidx.activity:activity-ktx:1.10.1")
        implementation("androidx.activity:activity-compose:1.10.1")
    }

    sourceSets.jvmMain.dependencies {
        implementation(compose.desktop.currentOs)
    }

    jvm().compilations.all {
        compileTaskProvider.configure {
            compilerOptions {
                freeCompilerArgs.add("-XXlenient-mode")
            }
        }
    }
}
