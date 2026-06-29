plugins {
    alias(libs.plugins.kotlinJvm)
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
    implementation("org.slf4j:slf4j-simple:2.0.18")
    implementation(libs.ktor.client.core)
    implementation(libs.kotlinx.serialization.json)
}

tasks.test {
    useJUnitPlatform()
}