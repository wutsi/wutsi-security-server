package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.exception.NotFoundException
import com.wutsi.platform.security.dao.KeyRepository
import com.wutsi.platform.security.dto.GetKeyResponse
import com.wutsi.platform.security.dto.Key
import com.wutsi.platform.security.util.ErrorURN.KEY_NOT_FOUND
import org.springframework.stereotype.Service

@Service
class GetKeyDelegate(private val dao: KeyRepository) {
    fun invoke(): GetKeyResponse {
        val keys = dao.findByActive(true)
            .sortedByDescending { it.id }
        if (keys.isEmpty())
            throw NotFoundException(
                error = Error(code = KEY_NOT_FOUND.urn)
            )

        val key = keys[0]
        return GetKeyResponse(
            key = Key(
                algorithm = key.algorithm,
                content = key.publicKey
            )
        )
    }
}
