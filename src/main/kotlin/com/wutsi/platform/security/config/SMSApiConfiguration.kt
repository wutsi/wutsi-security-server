package com.wutsi.platform.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.security.feign.FeignAuthorizationRequestInterceptor
import com.wutsi.platform.core.tracing.feign.FeignTracingRequestInterceptor
import com.wutsi.platform.security.service.ServiceTokenProvider
import com.wutsi.platform.sms.Environment.PRODUCTION
import com.wutsi.platform.sms.Environment.SANDBOX
import com.wutsi.platform.sms.WutsiSmsApi
import com.wutsi.platform.sms.WutsiSmsApiBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles

@Configuration
public class SMSApiConfiguration(
    private val tokenProvider: ServiceTokenProvider,
    private val tracingRequestInterceptor: FeignTracingRequestInterceptor,
    private val mapper: ObjectMapper,
    private val env: Environment
) {
    @Bean
    fun smsApi(): WutsiSmsApi =
        WutsiSmsApiBuilder().build(
            env = environment(),
            mapper = mapper,
            interceptors = listOf(
                tracingRequestInterceptor,
                FeignAuthorizationRequestInterceptor(tokenProvider)
            )
        )

    private fun environment(): com.wutsi.platform.sms.Environment =
        if (env.acceptsProfiles(Profiles.of("prod")))
            PRODUCTION
        else
            SANDBOX
}
