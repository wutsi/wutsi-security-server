package com.wutsi.platform.security.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.security.dao.ApplicationRepository
import com.wutsi.platform.security.dto.GrantScopeRequest
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/GrantScopeController.sql"])
public class GrantScopesControllerTest {
    @LocalServerPort
    public val port: Int = 0

    @Autowired
    private lateinit var dao: ApplicationRepository

    private val rest = RestTemplate()
    private lateinit var url: String

    @BeforeEach
    fun setUp() {
        url = "http://localhost:$port/v1/applications/1/scopes"
    }

    @Test
    public fun `grant scopes`() {
        val request = GrantScopeRequest(
            scopeNames = listOf("user-read-basic", "user-read-email", "user-read")
        )
        val response = rest.postForEntity(url, request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

//        val app = dao.findById(1).get()
//        assertEquals(3, app.scopes.size)
    }

    @Test
    public fun `grant invalid scope`() {
        val request = GrantScopeRequest(
            scopeNames = listOf("xxx")
        )
        val ex = assertThrows<HttpStatusCodeException> {
            rest.postForEntity(url, request, Any::class.java)
        }

        assertEquals(400, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.INVALID_SCOPE.urn, response.error.code)
    }

    @Test
    public fun `grant invalid application`() {
        url = "http://localhost:$port/v1/applications/9999/scopes"
        val request = GrantScopeRequest(
            scopeNames = listOf("user-read-basic", "user-read-email", "user-read")
        )
        val ex = assertThrows<HttpStatusCodeException> {
            rest.postForEntity(url, request, Any::class.java)
        }

        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.APPLICATION_NOT_FOUND.urn, response.error.code)
    }
}
