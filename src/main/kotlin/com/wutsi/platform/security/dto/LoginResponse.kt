package com.wutsi.platform.security.dto

import org.springframework.format.`annotation`.DateTimeFormat
import java.time.OffsetDateTime
import kotlin.String

public data class LoginResponse(
    public val accessToken: String = "",
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val created: OffsetDateTime = OffsetDateTime.now(),
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val expires: OffsetDateTime = OffsetDateTime.now()
)
