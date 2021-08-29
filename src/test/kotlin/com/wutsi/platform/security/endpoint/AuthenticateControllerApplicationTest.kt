package com.wutsi.platform.security.endpoint

import com.auth0.jwt.JWT
import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.security.dao.KeyRepository
import com.wutsi.platform.security.dao.LoginRepository
import com.wutsi.platform.security.dto.AuthenticationRequest
import com.wutsi.platform.security.dto.AuthenticationResponse
import com.wutsi.platform.security.service.jwt.JWTService
import com.wutsi.platform.security.util.ErrorURN
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/AuthenticateController.sql"])
public class AuthenticateControllerApplicationTest {
    @LocalServerPort
    public val port: Int = 0

    private val rest = RestTemplate()
    private lateinit var url: String

    @Autowired
    private lateinit var keyDao: KeyRepository

    @Autowired
    private lateinit var dao: LoginRepository

    @BeforeEach
    fun setUp() {
        url = "http://localhost:$port/v1/auth"
    }

    @Test
    public fun `login`() {
        val key = keyDao.findById(1).get()
        println("public: ${key.publicKey}")
        println("private: ${key.privateKey}")

        val request = AuthenticationRequest(
            type = "application",
            apiKey = "0000-1111"
        )
        val response = rest.postForEntity(url, request, AuthenticationResponse::class.java)

        assertEquals(200, response.statusCodeValue)
        assertTrue(response.body.accessToken.isNotEmpty())
        println(response.body.accessToken)

        // Login
        val login = dao.findById(response.body.id).get()
        assertEquals(response.body.accessToken, login.accessToken)
        assertTrue(login.active)
        assertEquals(response.body.created.toInstant().toEpochMilli(), login.created.toInstant().toEpochMilli())
        assertEquals(response.body.expires.toInstant().toEpochMilli(), login.expires.toInstant().toEpochMilli())
        assertEquals(1L, login.application?.id)

        // Verify
        val decoded = JWT.decode(response.body.accessToken)
        assertEquals(JWTService.ISSUER, decoded.issuer)
        assertEquals("urn:application:wutsi:1", decoded.subject)
        assertEquals(key.id.toString(), decoded.keyId)
        assertEquals(JWTService.APP_TOKEN_TTL_MILLIS / 60000, (decoded.expiresAt.time - decoded.issuedAt.time) / 60000)
        assertEquals("com.wutsi.application.test", decoded.claims["name"]?.asString())
        assertEquals(listOf("user-read-basic", "user-read-email"), decoded.claims["scope"]?.asList(String::class.java))
    }

    @Test
    fun `login with inactive app`() {
        val request = AuthenticationRequest(
            type = "application",
            apiKey = "inactive-key"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }

        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.APPLICATION_NOT_ACTIVE.urn, response.error.code)
    }

    @Test
    fun `login with invid api-key`() {
        val request = AuthenticationRequest(
            type = "application",
            apiKey = "????"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }

        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.APPLICATION_NOT_FOUND.urn, response.error.code)
    }

    @Test
    fun `login with no api-key`() {
        val request = AuthenticationRequest(
            type = "application",
            apiKey = null
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }

        assertEquals(400, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.AUTHENTICATION_API_KEY_REQUIRED.urn, response.error.code)
    }
}
