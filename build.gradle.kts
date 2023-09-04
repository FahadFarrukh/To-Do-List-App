plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.android.library") version "7.4.0" apply false

}

// Define the compose_version variable


buildscript {
    repositories {
        google()
        mavenCentral() // Adding the Google repository for dependency resolution
    }

    dependencies {
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
        classpath(kotlin("gradle-plugin", version = "1.9.0"))
    }
}