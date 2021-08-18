package com.wutsi.platform.security.util

class JWTPayload(
    val sub: String,
    val name: String,
    val scopes: String
)
