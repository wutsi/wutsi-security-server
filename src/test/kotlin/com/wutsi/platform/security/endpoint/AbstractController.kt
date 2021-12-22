package com.wutsi.platform.security.endpoint

import com.wutsi.platform.core.test.TestTracingContext
import com.wutsi.platform.core.tracing.spring.SpringTracingRequestInterceptor
import org.junit.jupiter.api.BeforeEach
import org.springframework.web.client.RestTemplate

abstract class AbstractController {
    companion object {
        const val TENANT_ID = 777L
    }

    protected lateinit var rest: RestTemplate

    @BeforeEach
    open fun setUp() {
        rest = RestTemplate()
        rest.interceptors.add(SpringTracingRequestInterceptor(TestTracingContext(tenantId = TENANT_ID.toString())))
    }
}
