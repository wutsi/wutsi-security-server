package com.wutsi.platform.security.service.connector

data class User(
    val id: Long = -1,
    val displayName: String? = null,
    val active: Boolean = true,
    val language: String = "en",
    val scopes: List<String> = emptyList(),
    val admin: Boolean = false,
)
