package com.wutsi.platform.security.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.OffsetDateTime

public data class Application(
    public val id: Long = 0,
    public val name: String = "",
    public val apiKey: String = "",
    public val title: String = "",
    public val description: String? = null,
    public val securityLevel: Int = 0,
    public val configUrl: String? = null,
    public val homeUrl: String? = null,
    public val active: Boolean = false,
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val created: OffsetDateTime = OffsetDateTime.now(),
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val updated: OffsetDateTime = OffsetDateTime.now(),
    public val scopes: List<Scope> = emptyList()
)
