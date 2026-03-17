package com.apimonitor

import com.apimonitor.controller.ExampleController
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest
import org.springframework.context.annotation.Bean
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.WebClient
import kotlin.math.floor
import kotlin.test.assertNotNull
import okhttp3.mockwebserver.MockWebServer

/*
    Testing concurrent vs sequential requests to the ExampleController to demonstrate
    non-blocking behavior and time comparison using WebClient
 */

@WebFluxTest(controllers = [ExampleController::class])
class NonBlockingControllerTest {
    companion object {
        const val NUM_REQUESTS = 30

        var concurrentTimes: Long? = null
        var sequentialTimes: Long? = null

        lateinit var mockServer: MockWebServer

        @BeforeAll
        @JvmStatic
        fun setupMockServer() {
            mockServer = MockWebServer()
            mockServer.start(8083)

            repeat(NUM_REQUESTS * 2) {
                mockServer.enqueue(MockResponse().setResponseCode(200).setBody("OK"))
            }
        }

        @AfterAll
        @JvmStatic
        fun shutdownMockServer() {
            mockServer.shutdown()
        }

        @AfterAll
        @JvmStatic
        fun compareTimes() {
            val concurrent = assertNotNull(concurrentTimes)
            val sequential = assertNotNull(sequentialTimes)

            val timeDifference = floor(sequential.toDouble() / concurrent.toDouble() * 100) / 100
            val faster = if (timeDifference > 1) "Concurrent requests" else "Sequential requests"

            println(
                "AfterAll: Sequential requests took $sequential ms - Concurrent requests took $concurrent ms, $faster were $timeDifference times faster",
            )
        }
    }

    @Autowired
    lateinit var webTestClient: WebTestClient

    @TestConfiguration
    class TestConfig {
        @Bean
        fun testApiWebClient(): WebClient = WebClient.builder().baseUrl("http://localhost:8083").build()
    }

    @Test
    fun `test concurrent requests`() =
        runBlocking {
            val urls = List(NUM_REQUESTS) { "/test" }

            val startTime = System.currentTimeMillis()
            val results =
                urls
                    .mapIndexed { index, url ->
                        async {
                            println("Request $index started concurrently on thread ${Thread.currentThread().name}")

                            val body =
                                webTestClient
                                    .get()
                                    .uri("/api$url")
                                    .exchange()
                                    .expectStatus()
                                    .isOk
                                    .expectBody(String::class.java)
                                    .returnResult()
                                    .responseBody

                            println("Request $index completed concurrently on thread ${Thread.currentThread().name}")

                            body
                        }
                    }.awaitAll()

            results.forEach { body ->
                assert(!body.isNullOrEmpty()) { "Response should not be null or empty" }
            }

            concurrentTimes = System.currentTimeMillis() - startTime
            println("Concurrent requests total time: ${System.currentTimeMillis() - startTime} ms")
        }

    @Test
    fun `test sequential requests`() =
        runBlocking {
            val urls = List(NUM_REQUESTS) { "/test" }

            val startTime = System.currentTimeMillis()

            urls.forEachIndexed { index, url ->
                println("Request $index started sequentially on thread ${Thread.currentThread().name}")

                webTestClient
                    .get()
                    .uri("/api$url")
                    .exchange()
                    .expectStatus()
                    .isOk
                    .expectBody(String::class.java)
                    .returnResult()
                    .responseBody
                println("Request $index completed sequentially on thread ${Thread.currentThread().name}")
            }

            sequentialTimes = System.currentTimeMillis() - startTime
            println("Sequential requests total time: ${System.currentTimeMillis() - startTime} ms")
        }
}
