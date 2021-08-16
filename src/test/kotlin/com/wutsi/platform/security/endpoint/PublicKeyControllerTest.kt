package com.wutsi.platform.security.endpoint

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import kotlin.Int

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PublicKeyControllerTest {
    @LocalServerPort
    public val port: Int = 0

    @Test
    public fun invoke() {
    }
}
