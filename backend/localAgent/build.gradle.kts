plugins {
    kotlin("jvm") version "2.3.20"
}

group = "org.api-monitor"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(projects.shared)

    implementation("com.github.ajalt.clikt:clikt:5.0.1")
    implementation("com.github.ajalt.clikt:clikt-markdown:5.0.1")
}

tasks.test {
    useJUnitPlatform()
}