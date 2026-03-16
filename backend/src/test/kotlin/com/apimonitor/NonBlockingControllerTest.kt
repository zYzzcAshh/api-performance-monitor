package com.apimonitor

import com.apimonitor.controllers.ExampleController
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest
import org.springframework.context.annotation.Bean
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.WebClient
import kotlin.math.floor
import kotlin.test.assertNotNull

/*
    Testing concurrent vs sequential requests to the ExampleController to demonstrate
    non-blocking behavior and time comparison using WebClient
 */
@WebFluxTest(controllers = [ExampleController::class])
class NonBlockingControllerTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @TestConfiguration
    class TestConfig {
        @Bean
        fun webClient(): WebClient = WebClient.builder().build()
    }

    @Test
    fun `test concurrent requests`() =
        runBlocking {
            val urls = List(NUM_REQUESTS) { "/example2" }

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
            val urls = List(NUM_REQUESTS) { "/example2" }

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

    companion object {
        const val NUM_REQUESTS = 5

        var concurrentTimes: Long? = null
        var sequentialTimes: Long? = null

        @AfterAll
        @JvmStatic
        fun compareTimes() {
            assertNotNull(concurrentTimes)
            assertNotNull(sequentialTimes)

            val timeDifference = floor(sequentialTimes!!.toDouble() / concurrentTimes!!.toDouble() * 100) / 100
            val faster = if (timeDifference > 0) concurrentTimes else sequentialTimes

            println(
                "AfterAll: Sequential requests took $sequentialTimes ms - Concurrent requests took $concurrentTimes ms, $faster was $timeDifference times faster",
            )
        }
    }
}
