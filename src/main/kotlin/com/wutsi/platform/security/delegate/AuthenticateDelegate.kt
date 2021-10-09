package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.security.dto.AuthenticationRequest
import com.wutsi.platform.security.dto.AuthenticationResponse
import com.wutsi.platform.security.service.auth.ApplicationAuthenticator
import com.wutsi.platform.security.service.auth.RunAsAuthenticator
import com.wutsi.platform.security.service.auth.SMSAuthenticator
import com.wutsi.platform.security.util.ErrorURN
import org.springframework.stereotype.Service

@Service
public class AuthenticateDelegate(
    private val applicationAuthenticator: ApplicationAuthenticator,
    private val smsAuthenticator: SMSAuthenticator,
    private val runAsAuthenticator: RunAsAuthenticator
) {
    fun invoke(request: AuthenticationRequest): AuthenticationResponse {
        val authenticator = when (request.type.toLowerCase()) {
            "application" -> applicationAuthenticator
            "sms" -> smsAuthenticator
            "runas" -> runAsAuthenticator
            else -> throw BadRequestException(
                error = Error(
                    code = ErrorURN.AUTHENTICATION_TYPE_NOT_SUPPORTED.urn,
                    parameter = Parameter(
                        name = "type",
                        value = request.type,
                        type = ParameterType.PARAMETER_TYPE_PAYLOAD
                    )
                )
            )
        }

        authenticator.validate(request)
        val login = authenticator.authenticate(request)
        return AuthenticationResponse(
            id = login.id ?: -1,
            accessToken = login.accessToken,
            created = login.created,
            expires = login.expires
        )
    }
}
