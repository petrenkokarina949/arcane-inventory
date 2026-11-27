plugins {
    kotlin("jvm") version "2.4.20"
}

group = "ie.setu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}
dependencies {
    implementation("tools.jackson.core:jackson-databind:3.1.4")
    implementation("tools.jackson.module:jackson-module-kotlin:3.1.4")

    testImplementation(kotlin("test"))
}