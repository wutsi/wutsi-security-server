package com.wutsi.platform.security.dao

import com.wutsi.platform.security.entity.MFALoginEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface MFALoginRepository : CrudRepository<MFALoginEntity, Long> {
    fun findByToken(token: String): Optional<MFALoginEntity>
}
