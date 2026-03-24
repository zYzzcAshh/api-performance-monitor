package pt.isel.api_pm.service

import kotlinx.coroutines.runBlocking
import pt.isel.api_pm.exceptions.DuplicateEndpointException
import pt.isel.api_pm.exceptions.InvalidIntervalException
import pt.isel.api_pm.exceptions.InvalidUrlException
import pt.isel.api_pm.repo.memory.EndpointRepositoryMemory
import kotlin.test.*

class EndpointServiceTests {
    private lateinit var service: EndpointService

    @BeforeTest
    fun setup() {
        val repo = EndpointRepositoryMemory()
        service = EndpointService(repo)
    }

    @Test
    fun `should create endpoint successfully`() =
        runBlocking {
            service.add(0, "https://api.github.com", "github", 60)

            val endpoints = service.getByUser(0)

            assertEquals(1, endpoints.size)
            assertEquals("https://api.github.com", endpoints.first().url)
        }

    @Test
    fun `should reject invalid url`() {
        runBlocking {
            assertFailsWith<InvalidUrlException> {
                service.add(0, "not-a-url", "test", 60)
            }
        }
    }

    @Test
    fun `should reject invalid interval`() {
        runBlocking {
            assertFailsWith<InvalidIntervalException> {
                service.add(0, "https://google.com", "test", 5)
            }
        }
    }

    @Test
    fun `should reject duplicate endpoint`() {
        runBlocking {
            service.add(0, "https://api.github.com", "github", 60)

            assertFailsWith<DuplicateEndpointException> {
                service.add(0, "https://api.github.com", "github", 60)
            }
        }
    }

    @Test
    fun `should return only endpoints of a user`() =
        runBlocking {
            service.add(0, "https://api.github.com", "github", 60)
            service.add(1, "https://google.com", "google", 60)

            val user0 = service.getByUser(0)
            val user1 = service.getByUser(1)

            assertEquals(1, user0.size)
            assertEquals(1, user1.size)
        }

    @Test
    fun `should treat urls with trailing slash as duplicate`() {
        runBlocking {
            service.add(0, "https://api.github.com/", "gh", 60)

            assertFailsWith<DuplicateEndpointException> {
                service.add(0, "https://api.github.com", "gh", 60)
            }
        }
    }

    @Test
    fun `should return empty list for new user`() =
        runBlocking {
            val endpoints = service.getByUser(999)
            assertTrue(endpoints.isEmpty())
        }
}
