package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.security.dao.KeyRepository
import com.wutsi.platform.security.dto.CreateKeyResponse
import com.wutsi.platform.security.entity.KeyEntity
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.time.OffsetDateTime
import java.util.Base64
import javax.transaction.Transactional

@Service
public class CreateKeyDelegate(
    private val dao: KeyRepository
) {
    companion object {
        const val ALGO = "RSA"
        const val KEY_SIZE = 2048
    }

    @Transactional
    fun invoke(): CreateKeyResponse {
        val key = createKey()
        deactivatePreviousKey(key)

        return CreateKeyResponse(
            id = key.id ?: -1
        )
    }

    private fun createKey(): KeyEntity {
        val keyPair = createKeyPair()
        val encoder = Base64.getEncoder()
        return dao.save(
            KeyEntity(
                algorithm = ALGO,
                active = true,
                created = OffsetDateTime.now(),
                expired = null,
                privateKey = encoder.encodeToString(keyPair.private.encoded),
                publicKey = encoder.encodeToString(keyPair.public.encoded)
            )
        )
    }

    private fun createKeyPair(): KeyPair {
        val generator = KeyPairGenerator.getInstance(ALGO)
        generator.initialize(KEY_SIZE)
        return generator.generateKeyPair()
    }

    private fun deactivatePreviousKey(current: KeyEntity) {
        val now = OffsetDateTime.now()
        val keys = dao.findByActive(true)
            .filter { it.id != current.id }
        if (keys.isEmpty())
            return

        keys.forEach {
            it.active = false
            it.expired = now
        }
        dao.saveAll(keys)
    }
}
