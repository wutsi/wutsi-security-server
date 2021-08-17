package com.wutsi.platform.security.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.security.dto.GetKeyResponse
import com.wutsi.platform.security.util.ErrorURN
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/GetKeyController.sql"])
public class GetKeyControllerTest {
    @LocalServerPort
    public val port: Int = 0

    private val rest = RestTemplate()
    private lateinit var url: String

    @BeforeEach
    fun setUp() {
        url = "http://localhost:$port/v1/keys"
    }

    @Test
    public fun invoke() {
        val response = rest.getForEntity(url, GetKeyResponse::class.java)
        assertEquals(200, response.statusCodeValue)

        assertEquals("RSA", response.body.key.algorithm)
        assertEquals("public-3", response.body.key.content)
    }

    @Test
    @Sql(value = ["/db/clean.sql"])
    public fun invokeKeyNotFound() {
        val ex = assertThrows<HttpClientErrorException> {
            rest.getForEntity(url, ErrorResponse::class.java)
        }

        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.KEY_NOT_FOUND.urn, response.error.code)
    }
}
