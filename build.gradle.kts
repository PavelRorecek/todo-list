buildscript {
    repositories {
        google()
        mavenCentral()

        // Android Build Server
        maven { url = uri("../nowinandroid-prebuilts/m2repository") }
    }
}

plugins {
    id("com.android.application") version "8.0.0-alpha11" apply false
    id("com.android.library") version "8.0.0-alpha11" apply false
    id("org.jetbrains.kotlin.android") version "1.7.20" apply false
}
