package com.wutsi.platform.security.dto

import kotlin.collections.List

public data class SearchScopeResponse(
    public val scopes: List<Scope> = emptyList()
)
