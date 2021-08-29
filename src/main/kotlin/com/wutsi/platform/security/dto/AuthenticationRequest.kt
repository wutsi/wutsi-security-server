package com.wutsi.platform.security.dto

import javax.validation.constraints.NotBlank
import kotlin.String

public data class AuthenticationRequest(
    @get:NotBlank
    public val type: String = "",
    public val apiKey: String? = null
)
