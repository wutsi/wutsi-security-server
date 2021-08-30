package com.wutsi.platform.security.service.auth

import com.wutsi.platform.security.dao.MFALoginRepository
import com.wutsi.platform.security.entity.MFALoginEntity
import com.wutsi.platform.security.entity.MFALoginType
import com.wutsi.platform.security.service.connector.User
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.Optional
import javax.transaction.Transactional

@Service
public class MFAService(private val dao: MFALoginRepository) {
    @Async
    @Transactional
    fun saveAsync(type: MFALoginType, user: User, token: String, verificationId: Long) {
        dao.save(
            MFALoginEntity(
                accountId = user.id,
                token = token,
                type = type,
                verificationId = verificationId,
                scopes = user.scopes.joinToString(separator = ",")
            )
        )
    }

    fun findByToken(token: String): Optional<MFALoginEntity> =
        dao.findByToken(token)
}