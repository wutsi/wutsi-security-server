package com.wutsi.platform.security.service.auth

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_PAYLOAD
import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.security.dao.ApplicationRepository
import com.wutsi.platform.security.dto.AuthenticationRequest
import com.wutsi.platform.security.entity.ApplicationEntity
import com.wutsi.platform.security.entity.LoginEntity
import com.wutsi.platform.security.service.LoginService
import com.wutsi.platform.security.util.ErrorURN
import org.springframework.stereotype.Service

@Service
class ApplicationAuthenticator(
    private val appDao: ApplicationRepository,
    private val loginService: LoginService
) : Authenticator {
    override fun validate(request: AuthenticationRequest) {
        if (request.apiKey.isNullOrEmpty())
            throw BadRequestException(
                error = Error(
                    code = ErrorURN.API_KEY_MISSING.urn,
                    parameter = Parameter(
                        name = "apiKey",
                        type = PARAMETER_TYPE_PAYLOAD
                    )
                )
            )
    }

    override fun authenticate(request: AuthenticationRequest): LoginEntity {
        val app = findApplication(request.apiKey!!)
        ensureActive(app)
        return loginService.login(app)
    }

    private fun findApplication(apiKey: String): ApplicationEntity =
        appDao.findByApiKey(apiKey)
            .orElseThrow {
                ConflictException(
                    error = Error(
                        code = ErrorURN.APPLICATION_NOT_FOUND.urn,
                        parameter = Parameter(
                            name = "apiKey",
                            type = PARAMETER_TYPE_PAYLOAD,
                            value = apiKey
                        )
                    )
                )
            }

    private fun ensureActive(app: ApplicationEntity) {
        if (!app.active)
            throw ConflictException(
                error = Error(
                    code = ErrorURN.APPLICATION_NOT_ACTIVE.urn
                )
            )
    }
}
