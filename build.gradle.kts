plugins {
    kotlin("jvm") version "2.2.10"
}

kotlin {
    compilerOptions {
        //freeCompilerArgs.add("-Xdebug")
    }
}

tasks.register("run", JavaExec::class) {
    mainClass.set("ImageCompareKt")
    classpath = sourceSets["main"].runtimeClasspath
}

dependencies {
    implementation("org.jetbrains.skiko:skiko-awt:0.9.22")
    implementation("org.jetbrains.skiko:skiko-awt-runtime-macos-arm64:0.9.22")
}
