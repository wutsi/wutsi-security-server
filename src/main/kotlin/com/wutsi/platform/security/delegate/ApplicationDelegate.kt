package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.security.dto.GetApplicationResponse
import org.springframework.stereotype.Service
import kotlin.String

@Service
public class ApplicationDelegate() {
    public fun invoke(xApiKey: String): GetApplicationResponse {
        TODO()
    }
}
