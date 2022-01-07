package com.wutsi.platform.security.entity

import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "T_LOGIN")
data class LoginEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val tenantId: Long? = null,
    val accessToken: String = "",
    var active: Boolean = true,
    val created: OffsetDateTime = OffsetDateTime.now(),
    val expires: OffsetDateTime = OffsetDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_fk")
    val application: ApplicationEntity? = null,

    val accountId: Long? = null
)
