@file:Suppress("OPT_IN_USAGE")

plugins {
    kotlin("multiplatform") version "2.0.20-RC2"
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvm().mainRun {
        args("First-Arg", "Second-Arg")
    }
    tasks.withType<JavaExec>().configureEach {
        standardInput = System.`in`
    }
}