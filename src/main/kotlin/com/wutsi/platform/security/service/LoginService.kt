package com.wutsi.platform.security.service

import com.auth0.jwt.JWT
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.core.security.SubjectType
import com.wutsi.platform.core.security.spring.ApplicationTokenProvider
import com.wutsi.platform.core.security.spring.jwt.JWTBuilder
import com.wutsi.platform.security.dao.LoginRepository
import com.wutsi.platform.security.entity.ApplicationEntity
import com.wutsi.platform.security.entity.LoginEntity
import com.wutsi.platform.security.entity.MFALoginEntity
import com.wutsi.platform.security.service.connector.User
import com.wutsi.platform.security.service.jwt.RSAKeyProviderImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class LoginService(
    private val keyProvider: RSAKeyProviderImpl,
    private val dao: LoginRepository,
    private val logger: KVLogger,
) {
    companion object {
        const val APP_TOKEN_TTL_MILLIS: Long = 60L * 84600000L // 60 days
        const val USER_TOKEN_TTL_MILLIS: Long = 1L * 84600000L // 1 day
        private val LOGGER = LoggerFactory.getLogger(LoginService::class.java)
    }

    private fun logToken(token: String) {
        try {
            val jwt = JWT.decode(token)
            logger.add("jwt_name", jwt.claims[JWTBuilder.CLAIM_NAME]?.asString())
            logger.add("jwt_sub", jwt.subject)
            logger.add("jwt_sub_type", jwt.claims[JWTBuilder.CLAIM_SUBJECT_TYPE]?.asString())
            logger.add("jwt_scope", jwt.claims[JWTBuilder.CLAIM_SCOPE]?.asString())
            logger.add("jwt_admin", jwt.claims[JWTBuilder.CLAIM_ADMIN]?.asString())
            logger.add("jwt_tenant_id", jwt.claims[JWTBuilder.CLAIM_TENANT_ID]?.asString())
            logger.add("jwt_header", jwt.header)
        } catch (ex: Exception) {
            LOGGER.warn("Unable to decode the JWT for logging: $token")
        }
    }

    fun login(app: ApplicationEntity): LoginEntity {
        val token = JWTBuilder(
            ttl = APP_TOKEN_TTL_MILLIS,
            admin = false,
            subjectType = SubjectType.APPLICATION,
            name = app.name,
            subject = app.id.toString(),
            keyProvider = keyProvider,
            scope = app.scopes
                .filter { it.active }
                .map { it.name }
                .sorted(),
        ).build()
        logToken(token)

        return dao.save(
            LoginEntity(
                accessToken = token,
                application = app,
                active = true,
                created = OffsetDateTime.now(),
                expires = OffsetDateTime.now().plusSeconds(APP_TOKEN_TTL_MILLIS / 1000)
            )
        )
    }

    fun login(mfa: MFALoginEntity): LoginEntity {
        val token = JWTBuilder(
            ttl = USER_TOKEN_TTL_MILLIS,
            admin = mfa.admin,
            subjectType = SubjectType.USER,
            name = mfa.displayName,
            subject = mfa.accountId.toString(),
            keyProvider = keyProvider,
            scope = mfa.scopes?.split(',')?.sorted() ?: emptyList(),
            phoneNumber = mfa.address,
            tenantId = mfa.tenantId
        ).build()
        logToken(token)

        return dao.save(
            LoginEntity(
                accessToken = token,
                accountId = mfa.accountId,
                tenantId = mfa.tenantId,
                active = true,
                created = OffsetDateTime.now(),
                expires = OffsetDateTime.now().plusSeconds(APP_TOKEN_TTL_MILLIS / 1000)
            )
        )
    }

    fun login(phoneNumber: String, user: User, tenantId: Long?): LoginEntity {
        val token = JWTBuilder(
            ttl = USER_TOKEN_TTL_MILLIS,
            admin = user.admin,
            subjectType = SubjectType.USER,
            name = user.displayName,
            subject = user.id.toString(),
            keyProvider = keyProvider,
            scope = user.scopes,
            phoneNumber = phoneNumber,
            tenantId = tenantId
        ).build()
        logToken(token)

        return dao.save(
            LoginEntity(
                accessToken = token,
                accountId = user.id,
                tenantId = tenantId,
                active = true,
                created = OffsetDateTime.now(),
                expires = OffsetDateTime.now().plusSeconds(APP_TOKEN_TTL_MILLIS / 1000)
            )
        )
    }
}
