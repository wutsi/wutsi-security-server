package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.security.dao.ApplicationRepository
import com.wutsi.platform.security.dao.LoginRepository
import com.wutsi.platform.security.dto.LoginRequest
import com.wutsi.platform.security.dto.LoginResponse
import com.wutsi.platform.security.entity.ApplicationEntity
import com.wutsi.platform.security.entity.LoginEntity
import com.wutsi.platform.security.service.jwt.JWTService
import com.wutsi.platform.security.service.jwt.RSAKeyProviderImpl
import com.wutsi.platform.security.util.ErrorURN
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
public class LoginDelegate(
    private val appDao: ApplicationRepository,
    private val dao: LoginRepository,
    private val keyProvider: RSAKeyProviderImpl,
    private val jwt: JWTService
) {
    fun invoke(request: LoginRequest): LoginResponse {
        val app = findApplication(request.apiKey)
        ensureActive(app)

        val token = jwt.createToken(app, keyProvider)
        val login = createLogin(token, app)
        return LoginResponse(
            id = login.id ?: -1,
            accessToken = token
        )
    }

    private fun createLogin(token: String, application: ApplicationEntity): LoginEntity =
        dao.save(
            LoginEntity(
                accessToken = token,
                application = application,
                active = true,
                created = OffsetDateTime.now(),
                expires = OffsetDateTime.now().plusSeconds(JWTService.APP_TOKEN_TTL_MILLIS / 1000)
            )
        )

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
