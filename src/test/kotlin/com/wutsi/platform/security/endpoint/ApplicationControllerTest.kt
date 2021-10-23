package com.wutsi.platform.security.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.core.security.spring.SpringApiKeyRequestInterceptor
import com.wutsi.platform.core.test.TestApiKeyProvider
import com.wutsi.platform.security.dto.GetApplicationResponse
import com.wutsi.platform.security.util.ErrorURN.APPLICATION_NOT_FOUND
import org.junit.jupiter.api.BeforeEach
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
@Sql(value = ["/db/clean.sql", "/db/ApplicationController.sql"])
public class ApplicationControllerTest {
    @LocalServerPort
    public val port: Int = 0

    private lateinit var rest: RestTemplate

    @BeforeEach
    fun setUp() {
        rest = RestTemplate()
    }

    @Test
    public fun `return an application`() {
        val url = "http://localhost:$port/v1/applications/me"

        val apiKey = "0000-1111"
        val apiKeyProvider = TestApiKeyProvider(apiKey)
        rest.interceptors.add(SpringApiKeyRequestInterceptor(apiKeyProvider))
        val response = rest.getForEntity(url, GetApplicationResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val app = response.body.application
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

        assertEquals(2, app.scopes.size)
        assertEquals("user-read-basic", app.scopes[0].name)
        assertEquals("user-read-email", app.scopes[1].name)
    }

    @Test
    public fun `return 404 for invalid Api-Key`() {
        val apiKey = "xxxxxx"
        val apiKeyProvider = TestApiKeyProvider(apiKey)
        rest.interceptors.add(SpringApiKeyRequestInterceptor(apiKeyProvider))
        val url = "http://localhost:$port/v1/applications/me"

        val ex = assertThrows<HttpClientErrorException> {
            rest.getForEntity(url, ErrorResponse::class.java)
        }

        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(APPLICATION_NOT_FOUND.urn, response.error.code)
    }
}