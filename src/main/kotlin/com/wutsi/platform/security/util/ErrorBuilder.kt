package com.wutsi.platform.security.util

import com.wutsi.platform.core.error.Error

object ErrorBuilder {
    fun build(code: String): Error =
        Error(
            code = "urn:error:wutsi:security:$code"
        )
}
