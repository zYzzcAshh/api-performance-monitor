package pt.isel.api_pm.service

import kotlinx.coroutines.runBlocking
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.exceptions.DuplicateEndpointException
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
    fun `should create endpoint successfully`() = runBlocking {
        service.add(
            0,
            EndpointUrl("https://api.github.com"),
            "github",
            IntervalSeconds(60)
        )

        val endpoints = service.getByUser(0)

        assertEquals(1, endpoints.size)
        assertEquals("https://api.github.com", endpoints.first().url.value)
    }

    @Test
    fun `should reject invalid url`() = runBlocking {
        val ex = assertFailsWith<IllegalArgumentException> {
            service.add(
                0,
                EndpointUrl("not-a-url"),
                "test",
                IntervalSeconds(60)
            )
        }

        assertTrue(ex.message!!.contains("URL"))
    }

    @Test
    fun `should reject invalid interval`() = runBlocking {
        assertFailsWith<IllegalArgumentException> {
            service.add(
                0,
                EndpointUrl("https://google.com"),
                "test",
                IntervalSeconds(5)
            )
        }
    }

    @Test
    fun `should reject duplicate endpoint`() = runBlocking {
        service.add(
            0,
            EndpointUrl("https://api.github.com"),
            "github",
            IntervalSeconds(60)
        )

        assertFailsWith<DuplicateEndpointException> {
            service.add(
                0,
                EndpointUrl("https://api.github.com"),
                "github",
                IntervalSeconds(60)
            )
        }
    }

    @Test
    fun `should return only endpoints of a user`() = runBlocking {
        service.add(
            0,
            EndpointUrl("https://api.github.com"),
            "github",
            IntervalSeconds(60)
        )

        service.add(
            1,
            EndpointUrl("https://google.com"),
            "google",
            IntervalSeconds(60)
        )

        val user0 = service.getByUser(0)
        val user1 = service.getByUser(1)

        assertEquals(1, user0.size)
        assertEquals(1, user1.size)
    }

    @Test
    fun `should treat urls with trailing slash as duplicate`(): Unit = runBlocking {
        service.add(
            0,
            EndpointUrl("https://api.github.com/"),
            "gh",
            IntervalSeconds(60)
        )

        assertFailsWith<DuplicateEndpointException> {
            service.add(
                0,
                EndpointUrl("https://api.github.com"),
                "gh",
                IntervalSeconds(60)
            )
        }
    }

    @Test
    fun `should return empty list for new user`() = runBlocking {
        val endpoints = service.getByUser(999)
        assertTrue(endpoints.isEmpty())
    }
}