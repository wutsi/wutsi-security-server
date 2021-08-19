package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.security.dto.GetKeyResponse
import com.wutsi.platform.security.dto.Key
import com.wutsi.platform.security.service.KeyService
import org.springframework.stereotype.Service

@Service
class GetKeyDelegate(private val service: KeyService) {
    fun invoke(id: Long): GetKeyResponse {
        val key = service.getKey(id)
        return GetKeyResponse(
            key = Key(
                algorithm = key.algorithm,
                content = key.publicKey
            )
        )
    }
}
