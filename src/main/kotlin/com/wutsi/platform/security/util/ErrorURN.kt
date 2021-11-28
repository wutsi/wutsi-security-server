package com.wutsi.platform.security.util

enum class ErrorURN(val urn: String) {
    API_KEY_MISSING("urn:wutsi:error:security:api-key-missing"),
    APPLICATION_ALREADY_EXIST("urn:wutsi:error:security:application-already-exist"),
    APPLICATION_NOT_ACTIVE("urn:wutsi:error:security:application-not-active"),
    APPLICATION_NOT_FOUND("urn:wutsi:error:security:application-not-found"),
    AUTHENTICATION_TYPE_NOT_SUPPORTED("urn:wutsi:error:security:authentication-type-not-supported"),
    AUTHENTICATION_TYPE_NOT_ALLOWED("urn:wutsi:error:security:authentication-type-not-allowed"),
    INVALID_SCOPE("urn:wutsi:error:security:invalid-scope"),
    KEY_NOT_FOUND("urn:wutsi:error:security:key-not-found"),
    MFA_REQUIRED("urn:wutsi:error:security:mfa-required"),
    MFA_INVALID_TOKEN("urn:wutsi:error:security:mfa-invalid-token"),
    MFA_VERIFICATION_FAILED("urn:wutsi:error:security:mfa-verification-failed"),
    PHONE_NUMBER_REQUIRED("urn:wutsi:error:security:phone-number-required"),
    SCOPE_ALREADY_EXIST("urn:wutsi:error:security:scope-already-exist"),
    USER_NOT_ACTIVE("urn:wutsi:error:security:user-not-active"),
    USER_NOT_FOUND("urn:wutsi:error:security:user-not-found"),
    VERIFICATION_CODE_MISSING("urn:wutsi:error:security:verification-code-missing"),
}
