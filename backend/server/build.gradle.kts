plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)

    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"

    application
}

group = "pt.isel.api_pm"
version = "1.0.0"
application {
    mainClass.set("pt.isel.api_pm.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.java.jwt)
    implementation("io.ktor:ktor-server-cors:3.4.1")
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation("com.password4j:password4j:1.8.4")
    implementation("io.ktor:ktor-server-swagger:3.4.1")
    implementation("io.ktor:ktor-server-openapi:3.4.1")
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(kotlin("test"))
}
