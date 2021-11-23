import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
}

group = "earth.seeds"
version = "1.0-SNAPSHOT"
val ktor_version: String = "1.6.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("dev.kord:kord-core:0.8.0-M5")
    implementation("com.sksamuel.hoplite:hoplite-core:1.4.7")
    implementation("com.sksamuel.hoplite:hoplite-yaml:1.4.7")
    implementation(kotlin("script-runtime"))
    implementation ("io.ktor:ktor-client-auth:$ktor_version")
    implementation("com.petersamokhin.notionsdk:notionsdk:0.0.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}