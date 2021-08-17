package com.wutsi.platform.security.entity

import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "T_SCOPE")
data class ScopeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String = "",
    val description: String? = null,
    val securityLevel: Int = 0,
    var active: Boolean = true,
    val created: OffsetDateTime = OffsetDateTime.now(),
    var updated: OffsetDateTime = OffsetDateTime.now()
)
