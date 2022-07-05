package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.dto.SearchApplicationResponse
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/SearchApplicationController.sql"])
class SearchApplicationsControllerTest {
    @LocalServerPort
    val port: Int = 0

    private val rest = RestTemplate()

    @Test
    fun `search by name`() {
        val url = "http://localhost:$port/v1/applications?name=com.wutsi.application.test&limit=2"
        val response = rest.getForEntity(url, SearchApplicationResponse::class.java)
        assertEquals(200, response.statusCodeValue)

        assertEquals(1, response.body.applications.size)

        val app = response.body.applications[0]
        assertEquals("com.wutsi.application.test", app.name)
        assertEquals("Test", app.title)
        assertEquals("0000-1111", app.apiKey)
        assertEquals("This is the description", app.description)
        assertEquals("https://test.herokuapp.com/config", app.configUrl)
        assertEquals("https://test.herokuapp.com", app.homeUrl)
        assertEquals(true, app.active)
        assertEquals(3, app.securityLevel)
        assertNotNull(app.created)
        assertNotNull(app.updated)
    }

    @Test
    fun `search all`() {
        val url = "http://localhost:$port/v1/applications?limit=20"
        val response = rest.getForEntity(url, SearchApplicationResponse::class.java)
        assertEquals(200, response.statusCodeValue)

        assertEquals(2, response.body.applications.size)
    }
}
