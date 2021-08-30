package com.wutsi.platform.security.util

import com.wutsi.platform.core.util.URN

enum class ErrorURN(val urn: String) {
    API_KEY_MISSING(URN.of("error", "security", "api-key-missing").value),
    APPLICATION_ALREADY_EXIST(URN.of("error", "security", "application-already-exist").value),
    APPLICATION_NOT_ACTIVE(URN.of("error", "security", "application-not-active").value),
    APPLICATION_NOT_FOUND(URN.of("error", "security", "application-not-found").value),
    AUTHENTICATION_TYPE_NOT_SUPPORTED(URN.of("error", "security", "authentication-type-not-supported").value),
    INVALID_SCOPE(URN.of("error", "security", "invalid-scope").value),
    KEY_NOT_FOUND(URN.of("error", "security", "key-not-found").value),
    MFA_REQUIRED(URN.of("error", "security", "mfa-required").value),
    MFA_INVALID_TOKEN(URN.of("error", "security", "mfa-invalid-token").value),
    MFA_VERIFICATION_FAILED(URN.of("error", "security", "mfa-verification-failed").value),
    PHONE_NUMBER_REQUIRED(URN.of("error", "security", "phone-number-required").value),
    SCOPE_ALREADY_EXIST(URN.of("error", "security", "scope-already-exist").value),
    USER_NOT_ACTIVE(URN.of("error", "security", "user-not-active").value),
    USER_NOT_FOUND(URN.of("error", "security", "user-not-found").value),
    VERIFICATION_CODE_MISSING(URN.of("error", "security", "verification-code-missing").value),
}
