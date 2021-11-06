package com.wutsi.platform.security.service.auth

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_PAYLOAD
import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.security.dao.ApplicationRepository
import com.wutsi.platform.security.dto.AuthenticationRequest
import com.wutsi.platform.security.entity.LoginEntity
import com.wutsi.platform.security.service.LoginService
import com.wutsi.platform.security.service.connector.WutsiConnector
import com.wutsi.platform.security.util.ErrorURN
import org.springframework.stereotype.Service

@Service
public class RunAsAuthenticator(
    private val connector: WutsiConnector,
    private val loginService: LoginService,
    private val appDao: ApplicationRepository
) : Authenticator {
    companion object {
        const val SCOPE_RUNAS: String = "auth-runas"
    }

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
        if (request.phoneNumber.isEmpty()) {
            throw BadRequestException(
                error = Error(
                    code = ErrorURN.PHONE_NUMBER_REQUIRED.urn,
                    parameter = Parameter(
                        name = "phoneNumber",
                        type = PARAMETER_TYPE_PAYLOAD
                    )
                )
            )
        }
    }

    override fun authenticate(request: AuthenticationRequest): LoginEntity {
        checkPermission(request)
        val user = connector.authenticate(request)
            ?: throw ConflictException(
                error = Error(
                    code = ErrorURN.USER_NOT_FOUND.urn
                )
            )

        if (!user.active)
            throw ConflictException(
                error = Error(
                    code = ErrorURN.USER_NOT_ACTIVE.urn
                )
            )

        return loginService.login(request.phoneNumber, user)
    }

    private fun checkPermission(request: AuthenticationRequest) {
        // Find application
        val app = appDao.findByApiKey(request.apiKey!!)
            .orElseThrow {
                ConflictException(
                    error = Error(
                        code = ErrorURN.AUTHENTICATION_TYPE_NOT_ALLOWED.urn
                    )
                )
            }

        // Ensure active
        if (!app.active)
            throw ConflictException(
                error = Error(
                    code = ErrorURN.APPLICATION_NOT_ACTIVE.urn
                )
            )

        // Check Permission
        app.scopes.find { it.name == SCOPE_RUNAS }
            ?: throw ConflictException(
                error = Error(
                    code = ErrorURN.AUTHENTICATION_TYPE_NOT_ALLOWED.urn
                )
            )
    }
}
