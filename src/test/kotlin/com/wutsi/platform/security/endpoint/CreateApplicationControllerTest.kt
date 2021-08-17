package com.wutsi.platform.security.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.security.dao.ApplicationRepository
import com.wutsi.platform.security.dto.CreateApplicationRequest
import com.wutsi.platform.security.dto.CreateApplicationResponse
import com.wutsi.platform.security.util.ErrorURN
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/ApplicationController.sql"])
public class CreateApplicationControllerTest {
    @LocalServerPort
    public val port: Int = 0

    @Autowired
    private lateinit var dao: ApplicationRepository

    private val rest = RestTemplate()
    private lateinit var url: String

    @BeforeEach
    fun setUp() {
        url = "http://localhost:$port/v1/applications"
    }

    @Test
    public fun `create application`() {
        val request = CreateApplicationRequest(
            name = "test-app",
            title = "test-app",
            description = "Sample application",
            iconUrl = "https://img.com/icon.png",
            homeUrl = "https://test-app.herokuapp.com/home",
            configUrl = "https://test-app.herokuapp.com/config",
            securityLevel = 99,
            scopeNames = listOf(
                "user-read-basic",
                "payment-read"
            )
        )
        val response = rest.postForEntity(url, request, CreateApplicationResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val app = dao.findById(response.body.id).get()
        assertEquals(request.name, app.name)
        assertEquals(response.body.apiKey, app.apiKey)
        assertEquals(request.title, app.title)
        assertEquals(request.description, app.description)
        assertEquals(request.configUrl, app.configUrl)
        assertEquals(request.homeUrl, app.homeUrl)
        assertEquals(request.iconUrl, app.iconUrl)
        assertEquals(true, app.active)
        assertEquals(request.securityLevel, app.securityLevel)
        assertNotNull(app.created)
        assertNotNull(app.updated)
    }

    @Test
    public fun `create application with invalid scope will fail`() {
        val request = CreateApplicationRequest(
            name = "test-app-invalid-scope",
            title = "test-app",
            description = "Sample application",
            iconUrl = "https://img.com/icon.png",
            homeUrl = "https://test-app.herokuapp.com/home",
            configUrl = "https://test-app.herokuapp.com/config",
            securityLevel = 99,
            scopeNames = listOf(
                "user-read-basic",
                "payment-read",
                "???-invalid-scope-???"
            )
        )
        val ex = assertThrows<HttpStatusCodeException> {
            rest.postForEntity(url, request, ErrorResponse::class.java)
        }

        assertEquals(400, ex.rawStatusCode)
        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.INVALID_SCOPE.urn, response.error.code)
    }

    @Test
    public fun `create application with duplicate name will fail`() {
        val request = CreateApplicationRequest(
            name = "com.wutsi.application.test",
            title = "test-app",
            description = "Sample application",
            iconUrl = "https://img.com/icon.png",
            homeUrl = "https://test-app.herokuapp.com/home",
            configUrl = "https://test-app.herokuapp.com/config",
            securityLevel = 99
        )
        val ex = assertThrows<HttpStatusCodeException> {
            rest.postForEntity(url, request, ErrorResponse::class.java)
        }

        assertEquals(409, ex.rawStatusCode)
        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.APPLICATION_ALREADY_EXIST.urn, response.error.code)
    }
}
