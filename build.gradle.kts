plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20" // Add serialization plugin
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.1.20") // Specify test dependency version
}

application {
    mainClass.set("ChatServerKt")
}

kotlin {
    jvmToolchain(17) // Specify JVM target
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "17"
    }
}