plugins {
    id("plugin.feature")
    id("plugin.koin")
    id("app.cash.paparazzi") version "1.2.0"
    kotlin("plugin.serialization") version "1.7.20"
}

android {
    namespace = "com.pavelrorecek.feature.todolist"
}

dependencies {
    implementation(project(":core-design"))
    implementation(Dependencies.Serialization.json)

    testImplementation(project(":core-test"))
}
