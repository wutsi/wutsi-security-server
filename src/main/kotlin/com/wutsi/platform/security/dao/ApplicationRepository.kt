package com.wutsi.platform.security.dao

import com.wutsi.platform.security.entity.ApplicationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ApplicationRepository : CrudRepository<ApplicationEntity, Long>
