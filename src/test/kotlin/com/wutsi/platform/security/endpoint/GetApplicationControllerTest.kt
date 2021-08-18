package com.wutsi.platform.security.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.security.dto.GetApplicationResponse
import com.wutsi.platform.security.util.ErrorURN
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/GetApplicationController.sql"])
public class GetApplicationControllerTest {
    @LocalServerPort
    public val port: Int = 0

    private val rest = RestTemplate()

    @Test
    public fun `return an application`() {
        val url = "http://localhost:$port/v1/applications/1"

        val response = rest.getForEntity(url, GetApplicationResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val app = response.body.application
        assertEquals("com.wutsi.application.test", app.name)
        assertEquals("Test", app.title)
        assertEquals("This is the description", app.description)
        assertEquals("https://test.herokuapp.com/config", app.configUrl)
        assertEquals("https://test.herokuapp.com", app.homeUrl)
        assertEquals(true, app.active)
        assertEquals(3, app.securityLevel)
        assertNotNull(app.created)
        assertNotNull(app.updated)

        assertEquals(2, app.scopes.size)
        assertEquals("user-read-basic", app.scopes[0].name)
        assertEquals("user-read-email", app.scopes[1].name)
    }

    @Test
    public fun `return 404 for invalid ID`() {
        val url = "http://localhost:$port/v1/applications/99999"

        val ex = assertThrows<HttpClientErrorException> {
            rest.getForEntity(url, ErrorResponse::class.java)
        }

        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.APPLICATION_NOT_FOUND.urn, response.error.code)
    }
}
