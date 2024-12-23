plugins {
    kotlin("jvm") version "2.1.0"
}

group = "org.juol.aoc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("org.jgrapht:jgrapht-core:1.1.0")
    implementation("org.jgrapht:jgrapht-ext:1.1.0")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}
