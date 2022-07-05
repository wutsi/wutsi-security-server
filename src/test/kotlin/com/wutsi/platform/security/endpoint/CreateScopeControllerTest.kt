package com.wutsi.platform.security.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.security.dao.ScopeRepository
import com.wutsi.platform.security.dto.CreateScopeRequest
import com.wutsi.platform.security.dto.CreateScopeResponse
import com.wutsi.platform.security.util.ErrorURN
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/CreateScopeController.sql"])
class CreateScopeControllerTest {
    @LocalServerPort
    val port: Int = 0

    @Autowired
    private lateinit var dao: ScopeRepository

    private val rest = RestTemplate()
    private lateinit var url: String

    @BeforeEach
    fun setUp() {
        url = "http://localhost:$port/v1/scopes"
    }

    @Test
    fun `create a scope`() {
        val request = CreateScopeRequest(
            name = "foo",
            description = "this is the description",
            securityLevel = 7
        )
        val response = rest.postForEntity(url, request, CreateScopeResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val scope = dao.findById(response.body.id).get()
        assertEquals(request.name, scope.name)
        assertEquals(request.description, scope.description)
        assertEquals(request.securityLevel, scope.securityLevel)
        assertTrue(scope.active)
    }

    @Test
    fun `create a scope with duplicate name`() {
        val request = CreateScopeRequest(
            name = "user-read",
            description = "this is the description",
            securityLevel = 7
        )
        val ex = assertThrows<HttpStatusCodeException> {
            rest.postForEntity(url, request, CreateScopeResponse::class.java)
        }

        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.SCOPE_ALREADY_EXIST.urn, response.error.code)
    }
}
