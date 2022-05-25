package com.wutsi.platform.security.service

import com.wutsi.platform.core.security.spring.ApiKeyAuthenticator
import com.wutsi.platform.security.dto.AuthenticationRequest
import com.wutsi.platform.security.service.auth.ApplicationAuthenticator
import org.springframework.stereotype.Service

@Service
class ApiKeyAuthenticatorImpl(
    private val auth: ApplicationAuthenticator
) : ApiKeyAuthenticator {
    override fun authenticate(apiKey: String): String =
        auth.authenticate(
            request = AuthenticationRequest(
                type = "application",
                apiKey = apiKey
            )
        ).accessToken
}
