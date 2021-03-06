package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.dto.Scope
import com.wutsi.platform.security.dto.SearchScopeResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/SearchScopesController.sql"])
class SearchScopesControllerTest {
    @LocalServerPort
    val port: Int = 0

    private val rest = RestTemplate()
    private lateinit var url: String

    @BeforeEach
    fun setUp() {
        url = "http://localhost:$port/v1/scopes"
    }

    @Test
    fun invoke() {
        val response = rest.getForEntity(url, SearchScopeResponse::class.java)
        assertEquals(200, response.statusCodeValue)

        assertEquals(4, response.body.scopes.size)
        assertScopeEquals("payment-read", "Read payment information", 99, true, response.body.scopes[0])
        assertScopeEquals("user-read", "Read user information", 99, false, response.body.scopes[1])
        assertScopeEquals("user-read-basic", "Read user basic information", 99, true, response.body.scopes[2])
        assertScopeEquals("user-read-email", "Read user email", 0, true, response.body.scopes[3])
    }

    private fun assertScopeEquals(
        name: String,
        description: String,
        securityLevel: Int,
        active: Boolean,
        scope: Scope
    ) {
        assertEquals(name, scope.name)
        assertEquals(description, scope.description)
        assertEquals(securityLevel, scope.securityLevel)
        assertEquals(active, scope.active)
    }
}
