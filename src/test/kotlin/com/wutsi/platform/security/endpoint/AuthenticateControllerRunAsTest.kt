package com.wutsi.platform.security.endpoint

import com.auth0.jwt.JWT
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.account.WutsiAccountApi
import com.wutsi.platform.account.dto.AccountSummary
import com.wutsi.platform.account.dto.SearchAccountResponse
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.core.security.SubjectType
import com.wutsi.platform.core.security.spring.jwt.JWTBuilder
import com.wutsi.platform.security.dao.LoginRepository
import com.wutsi.platform.security.dto.AuthenticationRequest
import com.wutsi.platform.security.dto.AuthenticationResponse
import com.wutsi.platform.security.service.LoginService
import com.wutsi.platform.security.service.connector.WutsiConnector
import com.wutsi.platform.security.util.ErrorURN
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/AuthenticateController.sql"])
class AuthenticateControllerRunAsTest {
    @LocalServerPort
    public val port: Int = 0

    private val rest = RestTemplate()
    private lateinit var url: String

    @Autowired
    private lateinit var dao: LoginRepository

    @MockBean
    private lateinit var accountApi: WutsiAccountApi

    @BeforeEach
    fun setUp() {
        url = "http://localhost:$port/v1/auth"
    }

    @Test
    fun `login`() {
        setupSearchAccount(id = 333, displayName = "Ray Sponsible")

        val request = AuthenticationRequest(
            type = "runas",
            apiKey = "0000-1111",
            phoneNumber = "+23799505678"
        )
        val response = rest.postForEntity(url, request, AuthenticationResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        // Login
        val login = dao.findById(response.body.id).get()
        assertEquals(response.body.accessToken, login.accessToken)
        assertTrue(login.active)
        assertEquals(response.body.created.toInstant().toEpochMilli(), login.created.toInstant().toEpochMilli())
        assertEquals(response.body.expires.toInstant().toEpochMilli(), login.expires.toInstant().toEpochMilli())
        assertNull(login.application)
        assertEquals(333L, login.accountId)
        assertEquals(response.body.accessToken, login.accessToken)

        // Verify
        val decoded = JWT.decode(response.body.accessToken)
        assertEquals("333", decoded.subject)
        assertEquals("1", decoded.keyId)
        assertEquals(SubjectType.USER.name, decoded.claims[JWTBuilder.CLAIM_SUBJECT_TYPE]?.asString())
        assertEquals("Ray Sponsible", decoded.claims[JWTBuilder.CLAIM_NAME]?.asString())
        assertEquals("+23799505678", decoded.claims[JWTBuilder.CLAIM_PHONE_NUMBER]?.asString())
        assertEquals(false, decoded.claims[JWTBuilder.CLAIM_ADMIN]?.asBoolean())
        assertEquals(WutsiConnector.SCOPES, decoded.claims[JWTBuilder.CLAIM_SCOPE]?.asList(String::class.java))
        assertEquals(LoginService.USER_TOKEN_TTL_MILLIS / 60000, (decoded.expiresAt.time - decoded.issuedAt.time) / 60000)
    }

    @Test
    fun `login with application without permission`() {
        setupSearchAccount(id = 333, displayName = "Ray Sponsible")

        val request = AuthenticationRequest(
            type = "runas",
            apiKey = "0000-7777",
            phoneNumber = "+23799505678"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }
        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.AUTHENTICATION_TYPE_NOT_ALLOWED.urn, response.error.code)
    }

    @Test
    fun `login with invalid application`() {
        setupSearchAccount(id = 333, displayName = "Ray Sponsible")

        val request = AuthenticationRequest(
            type = "runas",
            apiKey = "xxxx",
            phoneNumber = "+23799505678"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }
        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.AUTHENTICATION_TYPE_NOT_ALLOWED.urn, response.error.code)
    }

    @Test
    fun `login with inactive application`() {
        setupSearchAccount(id = 333, displayName = "Ray Sponsible")

        val request = AuthenticationRequest(
            type = "runas",
            apiKey = "inactive-key",
            phoneNumber = "+23799505678"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }
        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.APPLICATION_NOT_ACTIVE.urn, response.error.code)
    }

    @Test
    fun `login with invalid user`() {
        setupSearchAccount(found = false)

        val request = AuthenticationRequest(
            type = "runas",
            apiKey = "0000-1111",
            phoneNumber = "+23799505678"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }
        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.USER_NOT_FOUND.urn, response.error.code)
    }

    @Test
    fun `login with inactive user`() {
        setupSearchAccount(status = "suspended")

        val request = AuthenticationRequest(
            type = "runas",
            apiKey = "0000-1111",
            phoneNumber = "+23799505678"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }
        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.USER_NOT_ACTIVE.urn, response.error.code)
    }

    private fun setupSearchAccount(
        id: Long = 333,
        status: String = "active",
        found: Boolean = true,
        displayName: String = "Foo",
        superUser: Boolean = false
    ) {
        if (found) {
            val response = SearchAccountResponse(
                listOf(
                    AccountSummary(id = id, status = status, displayName = displayName, superUser = superUser)
                )
            )
            doReturn(response).whenever(accountApi).searchAccount(any())
        } else {
            val response = SearchAccountResponse(
                listOf()
            )
            doReturn(response).whenever(accountApi).searchAccount(any())
        }
    }
}
