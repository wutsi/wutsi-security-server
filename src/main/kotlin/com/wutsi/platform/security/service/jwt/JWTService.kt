package com.wutsi.platform.security.service.jwt

import com.auth0.jwt.interfaces.RSAKeyProvider
import com.wutsi.platform.core.security.SubjectType
import com.wutsi.platform.core.security.spring.jwt.JWTBuilder
import com.wutsi.platform.security.entity.ApplicationEntity
import com.wutsi.platform.security.entity.MFALoginEntity
import org.springframework.stereotype.Service

@Service
class JWTService {
    companion object {
        const val APP_TOKEN_TTL_MILLIS: Long = 60 * 84600000 // 60 days
        const val USER_TOKEN_TTL_MILLIS: Long = 1 * 84600000 // 1 day
    }

    fun createToken(app: ApplicationEntity, keyProvider: RSAKeyProvider): String =
        JWTBuilder(
            ttl = APP_TOKEN_TTL_MILLIS,
            admin = false,
            subjectType = SubjectType.APPLICATION,
            subjectName = app.name,
            subject = app.id.toString(),
            keyProvider = keyProvider,
            scope = app.scopes
                .filter { it.active }
                .map { it.name }
                .sorted(),
        ).build()

    fun createToken(mfa: MFALoginEntity, keyProvider: RSAKeyProvider): String =
        JWTBuilder(
            ttl = USER_TOKEN_TTL_MILLIS,
            admin = mfa.admin,
            subjectType = SubjectType.USER,
            subjectName = mfa.displayName ?: "",
            subject = mfa.accountId.toString(),
            keyProvider = keyProvider,
            scope = mfa.scopes?.split(',')?.sorted() ?: emptyList()
        ).build()
}
