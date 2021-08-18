package com.wutsi.platform.security.dto

import org.springframework.format.`annotation`.DateTimeFormat
import java.time.OffsetDateTime
import javax.validation.constraints.Size
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List

public data class Application(
    public val id: Long = 0,
    public val name: String = "",
    @get:Size(max = 30)
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
