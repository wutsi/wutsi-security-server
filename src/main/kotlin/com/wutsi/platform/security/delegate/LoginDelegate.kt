package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.security.dao.ApplicationRepository
import com.wutsi.platform.security.dto.LoginRequest
import com.wutsi.platform.security.dto.LoginResponse
import com.wutsi.platform.security.entity.ApplicationEntity
import com.wutsi.platform.security.service.jwt.JWTService
import com.wutsi.platform.security.service.jwt.RSAKeyProviderImpl
import com.wutsi.platform.security.util.ErrorURN
import org.springframework.stereotype.Service

@Service
public class LoginDelegate(
    private val appDao: ApplicationRepository,
    private val keyProvider: RSAKeyProviderImpl,
    private val jwt: JWTService
) {
    fun invoke(request: LoginRequest): LoginResponse {
        val app = findApplication(request.apiKey)
        ensureActive(app)

        return LoginResponse(
            accessToken = jwt.createToken(app, keyProvider)
        )
    }

    private fun findApplication(apiKey: String): ApplicationEntity =
        appDao.findByApiKey(apiKey)
            .orElseThrow {
                ConflictException(
                    error = Error(
                        code = ErrorURN.APPLICATION_NOT_FOUND.urn
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
