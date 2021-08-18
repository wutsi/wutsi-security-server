package com.wutsi.platform.security.util

import com.wutsi.platform.core.util.URN

enum class ErrorURN(val urn: String) {
    APPLICATION_ALREADY_EXIST(URN.of("error", "security", "application-already-exist").value),
    APPLICATION_NOT_FOUND(URN.of("error", "security", "application-not-found").value),
    KEY_NOT_FOUND(URN.of("error", "security", "key-not-found").value),
    INVALID_SCOPE(URN.of("error", "security", "invalid-scope").value)
}
