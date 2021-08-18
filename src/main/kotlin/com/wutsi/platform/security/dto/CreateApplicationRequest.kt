package com.wutsi.platform.security.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public data class CreateApplicationRequest(
    @get:NotBlank
    public val name: String = "",
    @get:NotBlank
    @get:Size(max = 30)
    public val title: String = "",
    public val description: String? = null,
    public val securityLevel: Int = 0,
    public val configUrl: String? = null,
    public val homeUrl: String? = null,
    public val scopeNames: List<String> = emptyList()
)
