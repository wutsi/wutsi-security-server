package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.SearchApplicationsDelegate
import com.wutsi.platform.security.dto.SearchApplicationResponse
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.RequestParam
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Int
import kotlin.String

@RestController
public class SearchApplicationsController(
    private val `delegate`: SearchApplicationsDelegate
) {
    @GetMapping("/v1/applications")
    public fun invoke(
        @RequestParam(name = "name", required = false) name: String? = null,
        @RequestParam(name = "limit", required = false, defaultValue = "20") limit: Int = 20,
        @RequestParam(name = "offset", required = false, defaultValue = "0") offset: Int = 0
    ): SearchApplicationResponse = delegate.invoke(name, limit, offset)
}
