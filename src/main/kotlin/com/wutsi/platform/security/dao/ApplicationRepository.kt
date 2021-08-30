package com.wutsi.platform.security.dao

import com.wutsi.platform.security.entity.ApplicationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ApplicationRepository : CrudRepository<ApplicationEntity, Long> {
    fun findByApiKey(apiKey: String): Optional<ApplicationEntity>
    fun findByName(name: String): Optional<ApplicationEntity>
}
