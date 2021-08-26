package com.wutsi.platform.security.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import kotlin.String
import kotlin.collections.List

public data class GrantScopeRequest(
    @get:NotNull
    @get:NotEmpty
    public val scopeNames: List<String> = emptyList()
)
