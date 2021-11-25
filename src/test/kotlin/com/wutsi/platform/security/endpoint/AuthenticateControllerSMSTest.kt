package com.wutsi.platform.security.endpoint

import com.auth0.jwt.JWT
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.account.WutsiAccountApi
import com.wutsi.platform.account.dto.AccountSummary
import com.wutsi.platform.account.dto.SearchAccountResponse
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.core.security.SubjectType
import com.wutsi.platform.core.security.spring.jwt.JWTBuilder
import com.wutsi.platform.security.dao.LoginRepository
import com.wutsi.platform.security.dao.MFALoginRepository
import com.wutsi.platform.security.dto.AuthenticationRequest
import com.wutsi.platform.security.dto.AuthenticationResponse
import com.wutsi.platform.security.service.LoginService
import com.wutsi.platform.security.service.connector.WutsiConnector
import com.wutsi.platform.security.util.ErrorURN
import com.wutsi.platform.sms.WutsiSmsApi
import com.wutsi.platform.sms.dto.SendVerificationResponse
import feign.FeignException
import feign.Request
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
import java.nio.charset.Charset
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/AuthenticateController.sql"])
class AuthenticateControllerSMSTest {
    @LocalServerPort
    val port: Int = 0

    private val rest = RestTemplate()
    private lateinit var url: String

    @Autowired
    private lateinit var dao: LoginRepository

    @Autowired
    private lateinit var mfaDao: MFALoginRepository

    @MockBean
    private lateinit var accountApi: WutsiAccountApi

    @MockBean
    private lateinit var smsApi: WutsiSmsApi

    @BeforeEach
    fun setUp() {
        url = "http://localhost:$port/v1/auth"
    }

    @Test
    fun `send validation code`() {
        setupSearchAccount(id = 333, displayName = "Ray Sponsible")
        setupSendSMSVerification(777)

        val request = AuthenticationRequest(
            type = "sms",
            phoneNumber = "+23799505678"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }
        assertEquals(403, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.MFA_REQUIRED.urn, response.error.code)

        Thread.sleep(1000)
        val token = response.error.data!!["mfaToken"].toString()
        val mfa = mfaDao.findByToken(token).get()
        assertEquals(777L, mfa.verificationId)
        assertEquals(333L, mfa.accountId)
        assertEquals("Ray Sponsible", mfa.displayName)
        assertEquals(WutsiConnector.SCOPES, mfa.scopes?.split(","))
        assertEquals(request.phoneNumber, mfa.address)
        assertTrue(mfa.admin)
    }

    @Test
    fun `do not send validation code when user not active`() {
        setupSearchAccount(status = "suspended")

        val request = AuthenticationRequest(
            type = "sms",
            phoneNumber = "+23799505678"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }
        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.USER_NOT_ACTIVE.urn, response.error.code)

        verify(smsApi, never()).sendVerification(any())
    }

    @Test
    fun `do not send validation code when no user found`() {
        setupSearchAccount(found = false)

        val request = AuthenticationRequest(
            type = "sms",
            phoneNumber = "+23799505678"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }
        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.USER_NOT_FOUND.urn, response.error.code)

        verify(smsApi, never()).sendVerification(any())
    }

    @Test
    fun `validate code`() {
        val request = AuthenticationRequest(
            type = "sms",
            mfaToken = "0000000",
            verificationCode = "44344"
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
        assertNull(login.application)
        assertEquals(33L, login.accountId)

        // Verify
        val decoded = JWT.decode(response.body.accessToken)
        assertEquals("33", decoded.subject)
        assertEquals("1", decoded.keyId)
        assertEquals(SubjectType.USER.name, decoded.claims[JWTBuilder.CLAIM_SUBJECT_TYPE]?.asString())
        assertEquals("Ray Sponsible", decoded.claims[JWTBuilder.CLAIM_NAME]?.asString())
        assertEquals("+23799509999", decoded.claims[JWTBuilder.CLAIM_PHONE_NUMBER]?.asString())
        assertEquals(true, decoded.claims[JWTBuilder.CLAIM_ADMIN]?.asBoolean())
        assertEquals(listOf("payment-read", "user-read"), decoded.claims[JWTBuilder.CLAIM_SCOPE]?.asList(String::class.java))
        assertEquals(LoginService.USER_TOKEN_TTL_MILLIS / 60000, (decoded.expiresAt.time - decoded.issuedAt.time) / 60000)
    }

    @Test
    fun `validation fails on SMS validation failure`() {
        val req = Request.create(Request.HttpMethod.POST, "https://www.google.ca", emptyMap(), ByteArray(1), Charset.defaultCharset(), null)
        val fex = FeignException.Conflict("foo", req, ByteArray(1), emptyMap())
        doThrow(fex).whenever(smsApi).validateVerification(any(), any())

        val request = AuthenticationRequest(
            type = "sms",
            mfaToken = "0000000",
            verificationCode = "44344"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }
        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.MFA_VERIFICATION_FAILED.urn, response.error.code)
    }

    @Test
    fun `validation fails on invalid token`() {
        val request = AuthenticationRequest(
            type = "sms",
            mfaToken = "???",
            verificationCode = "454"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }
        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.MFA_INVALID_TOKEN.urn, response.error.code)
    }

    @Test
    fun `validation fails on missing code`() {
        val request = AuthenticationRequest(
            type = "sms",
            mfaToken = "0000000",
            verificationCode = ""
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, AuthenticationResponse::class.java)
        }
        assertEquals(400, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.VERIFICATION_CODE_MISSING.urn, response.error.code)
    }

    private fun setupSearchAccount(id: Long = 333, status: String = "active", found: Boolean = true, displayName: String = "Foo") {
        if (found) {
            val response = SearchAccountResponse(
                listOf(
                    AccountSummary(id = id, status = status, displayName = displayName, superUser = true)
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

    private fun setupSendSMSVerification(id: Long) {
        val response = SendVerificationResponse(id = id)
        doReturn(response).whenever(smsApi).sendVerification(any())
    }
}
