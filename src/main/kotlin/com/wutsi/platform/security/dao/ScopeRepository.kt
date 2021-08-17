package com.wutsi.platform.security.dao

import com.wutsi.platform.security.entity.ScopeEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ScopeRepository : CrudRepository<ScopeEntity, Long> {
    fun findByActive(active: Boolean): List<ScopeEntity>
}
