import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    kotlin("multiplatform") version "2.0.20"
    kotlin("plugin.compose") version "2.0.20"
    id("org.jetbrains.compose") version "1.6.11"
}

val app = project.configurations.create("app") {
    isCanBeResolved = true
    isCanBeConsumed = false
    attributes {
        attribute(KotlinPlatformType.attribute, KotlinPlatformType.jvm)
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    }
}

dependencies {
    app(project(":app")) {
        isTransitive = false
    }
}

kotlin {
    jvmToolchain(17)
    jvm()

    sourceSets.jvmMain.dependencies {
        implementation("io.sellmair:evas:1.0.0")
        implementation("io.sellmair:evas-compose:1.0.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.9.0")
        implementation(compose.desktop.currentOs)
        implementation(compose.foundation)
        implementation(compose.material3)
        runtimeOnly(project(":app-core"))
    }
}

tasks.withType<JavaExec>().configureEach {
    inputs.files(app)

    doFirst {
        systemProperty("app.cp", app.joinToString(File.pathSeparator))
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}