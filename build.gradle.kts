plugins {
    kotlin("jvm") version "2.0.20"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.12"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.12")
}

benchmark {
    targets.create("main")
}

