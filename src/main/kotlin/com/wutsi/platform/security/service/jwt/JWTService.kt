package com.wutsi.platform.security.service.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.RSAKeyProvider
import com.wutsi.platform.core.util.URN
import com.wutsi.platform.security.entity.ApplicationEntity
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JWTService {
    companion object {
        const val ISSUER = "Wutsi"
        const val APP_TOKEN_TTL: Long = 60 * 84600000   // 60 days
    }

    fun createToken(app: ApplicationEntity, keyProvider: RSAKeyProvider): String {
        val now = System.currentTimeMillis()
        val ttl = APP_TOKEN_TTL
        val scope = app.scopes
            .filter { it.active }
            .map { it.name }
            .sorted()

        return JWT.create()
            .withIssuer(ISSUER)
            .withIssuedAt(Date(now))
            .withExpiresAt(Date(now + ttl))
            .withJWTId(keyProvider.privateKeyId)
            .withSubject(URN.of("application", app.id.toString()).toString())
            .withClaim("name", app.name)
            .withClaim("scope", scope)
            .sign(Algorithm.RSA256(keyProvider))
    }
}
