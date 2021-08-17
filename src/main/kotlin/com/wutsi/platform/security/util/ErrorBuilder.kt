package com.wutsi.platform.security.util

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.util.URN

object ErrorBuilder {
    fun build(name: String): Error =
        Error(
            code = URN.of("error", "security", name).value
        )
}
