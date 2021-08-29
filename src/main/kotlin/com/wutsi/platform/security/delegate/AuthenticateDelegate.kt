package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.security.dto.AuthenticationRequest
import com.wutsi.platform.security.dto.AuthenticationResponse
import com.wutsi.platform.security.service.auth.ApplicationAuthenticator
import com.wutsi.platform.security.util.ErrorURN
import org.springframework.stereotype.Service

@Service
public class AuthenticateDelegate(
    private val applicationAuthenticator: ApplicationAuthenticator
) {
    fun invoke(request: AuthenticationRequest): AuthenticationResponse {
        val authenticator = when (request.type.toLowerCase()) {
            "application" -> applicationAuthenticator
            else -> throw BadRequestException(
                error = Error(
                    code = ErrorURN.AUTHENTICATION_TYPE_INVALID.urn,
                    parameter = Parameter(
                        name = "type",
                        value = request.type,
                        type = ParameterType.valueOf(request.type)
                    )
                )
            )
        }

        authenticator.validate(request)
        return authenticator.authenticate(request)
    }
}
