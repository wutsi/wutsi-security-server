package com.wutsi.platform.security.dao

import com.wutsi.platform.security.entity.LoginEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface LoginRepository : CrudRepository<LoginEntity, Long> {
    fun findByAccessToken(accessToken: String): Optional<LoginEntity>
}
