package com.wutsi.platform.security.dto

public data class SearchApplicationResponse(
    public val applications: List<ApplicationSummary> = emptyList()
)
