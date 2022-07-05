package com.wutsi.platform.security.endpoint

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.core.security.TokenProvider
import com.wutsi.platform.core.security.spring.SpringAuthorizationRequestInterceptor
import com.wutsi.platform.security.dao.LoginRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/LogoutController.sql"])
class LogoutControllerTest : AbstractController() {
    @LocalServerPort
    val port: Int = 0

    private lateinit var url: String

    @Autowired
    private lateinit var dao: LoginRepository

    @BeforeEach
    override fun setUp() {
        super.setUp()

        url = "http://localhost:$port/v1/logout"
    }

    @Test
    fun logout() {
        addTracingRequestInterceptor("1111")

        val response = rest.getForEntity(url, Any::class.java)

        assertEquals(200, response.statusCodeValue)
        val login = dao.findByAccessToken("1111").get()
        assertFalse(login.active)
    }

    @Test
    fun invalidToken() {
        addTracingRequestInterceptor("xxxxxxx")

        val response = rest.getForEntity(url, Any::class.java)

        assertEquals(200, response.statusCodeValue)
    }

    @Test
    fun noAccessToken() {
        val response = rest.getForEntity(url, Any::class.java)

        assertEquals(200, response.statusCodeValue)
    }

    private fun addTracingRequestInterceptor(accessToken: String) {
        val tokenProvider = mock<TokenProvider>()
        doReturn(accessToken).whenever(tokenProvider).getToken()

        rest.interceptors.add(SpringAuthorizationRequestInterceptor(tokenProvider))
    }
}
