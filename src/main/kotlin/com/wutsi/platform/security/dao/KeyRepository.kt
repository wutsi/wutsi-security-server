package com.wutsi.platform.security.dao

import com.wutsi.platform.security.entity.KeyEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface KeyRepository : CrudRepository<KeyEntity, Long> {
    fun findByActive(active: Boolean): List<KeyEntity>
}
