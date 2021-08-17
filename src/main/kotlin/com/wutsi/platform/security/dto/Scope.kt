package com.wutsi.platform.security.dto

import kotlin.Int
import kotlin.Long
import kotlin.String

public data class Scope(
    public val id: Long = 0,
    public val name: String = "",
    public val description: String? = null,
    public val securityLevel: Int = 0
)
