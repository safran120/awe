pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.google.com") }
        maven { url = uri("https://dl.google.com/dl/android/maven2/") }
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "PulseChat"
include(":app")
