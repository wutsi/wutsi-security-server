package com.wutsi.platform.security.dto

import kotlin.Long
import kotlin.String

public data class LoginResponse(
    public val id: Long = 0,
    public val accessToken: String = ""
)
