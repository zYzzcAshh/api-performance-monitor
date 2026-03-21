package pt.isel.api_pm

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import pt.isel.api_pm.app.module
import kotlin.test.*

class ApplicationTest {
    companion object {
        const val NUM_REQUESTS = 1000

        var concurrentTime = 0L
        var sequentialTime = 0L

        @AfterClass
        @JvmStatic
        fun afterAll() {
            println(
                "AfterAll: concurrentTime - $concurrentTime ms ; sequentialTime - $sequentialTime ms ; concurrentTime were ${"%.2f".format(
                    sequentialTime.toDouble() / concurrentTime,
                )} times faster than sequentialTime",
            )
        }
    }

    @Test
    fun testRoot() =
        testApplication {
            application {
                module()
            }
            val response = client.get("/")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("Ktor: ${Greeting().greet()}", response.bodyAsText())
        }

    @Test
    fun concurrentRequests() =
        testApplication {
            application {
                module(externalRequest = { 200 to "OK" })
            }

            val start = System.currentTimeMillis()
            val results =
                runBlocking {
                    (1..NUM_REQUESTS)
                        .map { idx ->
                            async {
                                val resp: HttpResponse = client.get("/api/test")
                                val status = resp.status
                                val body = resp.bodyAsText()
                                val threadName = Thread.currentThread().name
                                println("concurrent request $idx executed on thread: $threadName")
                                Triple(status, body, threadName)
                            }
                        }.awaitAll()
                }
            val concurrentMillis = System.currentTimeMillis() - start

            results.forEach { (status, body, _) ->
                assertEquals(HttpStatusCode.OK, status)
                assertEquals("OK", body)
            }

            println("concurrentRequests took $concurrentMillis ms")
            concurrentTime = concurrentMillis
        }

    @Test
    fun sequentialRequests() =
        testApplication {
            application {
                module(externalRequest = { 200 to "OK" })
            }

            val start = System.currentTimeMillis()
            val results = mutableListOf<HttpResponse>()
            for (i in 1..NUM_REQUESTS) {
                val resp: HttpResponse = client.get("/api/test")
                val threadName = Thread.currentThread().name
                println("sequential request $i executed on thread: $threadName")
                results += resp
            }
            val sequentialMillis = System.currentTimeMillis() - start

            results.forEach {
                assertEquals(HttpStatusCode.OK, it.status)
                assertEquals("OK", it.bodyAsText())
            }

            println("sequentialRequests took $sequentialMillis ms")
            sequentialTime = sequentialMillis
        }
}
