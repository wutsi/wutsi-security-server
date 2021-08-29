package com.wutsi.platform.security.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.OffsetDateTime

public data class AuthenticationResponse(
    public val id: Long = 0,
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val created: OffsetDateTime = OffsetDateTime.now(),
    public val expires: OffsetDateTime = OffsetDateTime.now(),
    public val accessToken: String = ""
)
