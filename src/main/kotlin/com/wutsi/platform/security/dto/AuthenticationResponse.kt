package com.wutsi.platform.security.dto

import org.springframework.format.`annotation`.DateTimeFormat
import java.time.OffsetDateTime
import kotlin.Long
import kotlin.String

public data class AuthenticationResponse(
    public val id: Long = 0,
    public val type: String = "",
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val expires: OffsetDateTime = OffsetDateTime.now(),
    public val accessToken: String = ""
)
