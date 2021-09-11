package com.wutsi.platform.security.service

import com.wutsi.platform.core.security.TokenProvider
import com.wutsi.platform.security.dao.ApplicationRepository
import com.wutsi.platform.security.dto.AuthenticationRequest
import com.wutsi.platform.security.service.auth.ApplicationAuthenticator
import org.springframework.stereotype.Service

@Service
class ApplicationTokenProvider(
    private val dao: ApplicationRepository,
    private val authenticator: ApplicationAuthenticator
) : TokenProvider {
    companion object {
        private const val APP_NAME = "com.wutsi.wutsi-security"
    }

    private var accessToken: String? = null

    override fun getToken(): String? {
        if (accessToken == null)
            authenticate()

        return accessToken
    }

    private fun authenticate() {
        val app = dao.findByName(APP_NAME).get()
        val response = authenticator.authenticate(
            AuthenticationRequest(
                type = "application",
                apiKey = app.apiKey
            )
        )

        this.accessToken = response.accessToken
    }
}
