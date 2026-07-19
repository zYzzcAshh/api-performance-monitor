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
    mainClass.set("pt.isel.api_pm.app.ApplicationKt")

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
    implementation("io.ktor:ktor-server-websockets")
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation("com.password4j:password4j:1.8.4")
    implementation("com.sun.mail:jakarta.mail:2.0.2")
    implementation("io.ktor:ktor-server-swagger:3.4.1")
    implementation("io.ktor:ktor-server-openapi:3.4.1")
    implementation("org.jetbrains.exposed:exposed-core:1.3.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.3.0")
    implementation("org.jetbrains.exposed:exposed-dao:1.3.0")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:1.3.0")
    implementation("com.h2database:h2:2.4.240")
    implementation("io.ktor:ktor-server-core:3.4.1")
    implementation("io.ktor:ktor-server-sse:3.4.1")
    implementation("io.ktor:ktor-server-core:3.4.1")
    implementation("io.ktor:ktor-server-sse:3.4.1")
    implementation("org.postgresql:postgresql:42.7.7")

    testImplementation(libs.ktor.serverTestHost)
    testImplementation(kotlin("test"))

    testImplementation("io.ktor:ktor-client-mock:3.4.1")
    testImplementation("io.ktor:ktor-client-content-negotiation:3.4.1")
    testImplementation("io.ktor:ktor-serialization-kotlinx-json:3.4.1")
    testImplementation("com.h2database:h2:2.3.232")
}

tasks.withType<Test> {
    val envFile = rootProject.file(".env")
    if (envFile.exists()) {
        envFile.forEachLine { line ->
            val trimmed = line.trim()
            if (trimmed.isNotEmpty() && !trimmed.startsWith("#")) {
                val (key, value) = trimmed.split("=", limit = 2)
                environment(key.trim(), value.trim())
            }
        }
    }
}
