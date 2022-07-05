package com.wutsi.platform.security.endpoint

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticateControllerTest {
    @LocalServerPort
    val port: Int = 0

    @Test
    fun invoke() {
    }
}
