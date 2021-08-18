package com.wutsi.platform.security.dao

import com.wutsi.platform.security.entity.LoginEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LoginRepository : CrudRepository<LoginEntity, Long>
