package com.wutsi.platform.security.dao

import com.wutsi.platform.security.entity.ScopeEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ScopeRepository : CrudRepository<ScopeEntity, Long> {
    fun findByNameIgnoreCase(name: String): Optional<ScopeEntity>
}
