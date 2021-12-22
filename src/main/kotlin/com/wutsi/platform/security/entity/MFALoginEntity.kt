package com.wutsi.platform.security.entity

import com.wutsi.platform.security.entity.MFALoginType.MFA_LOGIN_TYPE_INVALID
import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "T_MFA_LOGIN")
data class MFALoginEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated
    val type: MFALoginType = MFA_LOGIN_TYPE_INVALID,

    val token: String = "",
    val verificationId: Long = -1,
    val created: OffsetDateTime = OffsetDateTime.now(),
    val accountId: Long = -1,
    val tenantId: Long? = null,
    val displayName: String? = null,
    val scopes: String? = null,
    val admin: Boolean = false,
    val address: String = ""
)
