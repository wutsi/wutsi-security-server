package com.wutsi.platform.security.dto

import javax.validation.constraints.NotBlank
import kotlin.Int
import kotlin.String

public data class CreateScopeRequest(
    @get:NotBlank
    public val name: String = "",
    public val description: String = "",
    public val securityLevel: Int = 0
)
