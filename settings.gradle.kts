pluginManagement {
    repositories {
        mavenCentral()
        google()
    }

    plugins {
        kotlin("multiplatform") version "2.0.20"
        kotlin("jvm") version "2.0.20"
        kotlin("plugin.compose") version "2.0.20"
        id("org.jetbrains.compose") version "1.6.11"
        id("com.android.application") version "8.5.2"
    }
}

dependencyResolutionManagement {

    versionCatalogs {
        create("deps") {
            from(files("dependencies.toml"))
        }
    }

    repositories {
        mavenLocal {
            mavenContent {
                includeGroup("io.sellmair")
            }
        }
        mavenCentral()
        google()
    }
}

include(":joke-app")