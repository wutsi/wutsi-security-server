package com.wutsi.platform.security.service

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.exception.NotFoundException
import com.wutsi.platform.security.dao.KeyRepository
import com.wutsi.platform.security.entity.KeyEntity
import com.wutsi.platform.security.util.ErrorURN.KEY_NOT_FOUND
import org.springframework.stereotype.Service

@Service
class KeyService(
    private val dao: KeyRepository,
) {
    fun getKey(): KeyEntity {
        val keys = dao.findByActive(true)
            .sortedByDescending { it.id }
        if (keys.isEmpty())
            throw NotFoundException(
                error = Error(code = KEY_NOT_FOUND.urn)
            )
        return keys[0]
    }

    fun getKey(id: Long): KeyEntity =
        dao.findById(id)
            .orElseThrow {
                throw NotFoundException(
                    error = Error(code = KEY_NOT_FOUND.urn)
                )
            }
}
