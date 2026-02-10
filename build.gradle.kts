import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.10"
    kotlin("plugin.serialization") version "2.3.10" // Add serialization plugin
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.21.0")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.3.10") // Specify test dependency version
}

application {
    mainClass.set("ChatServerKt")
}

kotlin {
    jvmToolchain(17) // Specify JVM target
}

tasks.compileKotlin {
    compilerOptions.jvmTarget = JvmTarget.JVM_17
}
