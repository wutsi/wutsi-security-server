package com.wutsi.platform.security.util

import com.wutsi.platform.core.util.URN

enum class ErrorURN(val urn: String) {
    AUTHENTICATION_API_KEY_REQUIRED(URN.of("error", "security", "auth-api-key-required").value),
    AUTHENTICATION_TYPE_INVALID(URN.of("error", "security", "auth-type-invalid").value),
    APPLICATION_ALREADY_EXIST(URN.of("error", "security", "application-already-exist").value),
    APPLICATION_NOT_ACTIVE(URN.of("error", "security", "application-no-active").value),
    APPLICATION_NOT_FOUND(URN.of("error", "security", "application-not-found").value),
    SCOPE_ALREADY_EXIST(URN.of("error", "security", "scope-already-exist").value),
    KEY_NOT_FOUND(URN.of("error", "security", "key-not-found").value),
    INVALID_SCOPE(URN.of("error", "security", "invalid-scope").value)
}
