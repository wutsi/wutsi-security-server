package com.wutsi.platform.security.dto

import javax.validation.constraints.NotBlank
import kotlin.String

public data class LoginRequest(
    @get:NotBlank
    public val apiKey: String = ""
)
