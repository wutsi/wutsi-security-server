package com.wutsi.platform.security.util

class JWTHeader(
    val alg: String,
    val typ: String = "JWT"
)
