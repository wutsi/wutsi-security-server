package com.wutsi.platform.security.dto

import kotlin.collections.List

public data class SearchApplicationResponse(
    public val applications: List<ApplicationSummary> = emptyList()
)
