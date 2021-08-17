package com.wutsi.platform.security.entity

import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "T_APPLICATION")
data class ApplicationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val apiKey: String = "",
    val name: String = "",
    val description: String? = null,
    val title: String = "",
    val securityLevel: Int = 0,
    val active: Boolean = true,
    val configUrl: String? = null,
    val homeUrl: String? = null,
    val iconUrl: String? = null,
    val created: OffsetDateTime = OffsetDateTime.now(),
    val updated: OffsetDateTime = OffsetDateTime.now(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "T_APPLICATION_SCOPE",
        joinColumns = arrayOf(JoinColumn(name = "application_fk")),
        inverseJoinColumns = arrayOf(JoinColumn(name = "scope_fk"))
    )
    val scopes: List<ScopeEntity> = emptyList()
)
