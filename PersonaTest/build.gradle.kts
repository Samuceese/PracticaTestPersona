plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("app.cash.sqldelight") version "2.0.2"
    application
    id("org.jetbrains.dokka") version "1.9.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("org.mybatis:mybatis:3.5.13")
    implementation("org.lighthousegames:logging:1.3.0")
    implementation("ch.qos.logback:logback-classic:1.5.0")
    implementation("io.github.pdvrieze.xmlutil:core-jvm:0.86.3")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta9")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.10")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}


