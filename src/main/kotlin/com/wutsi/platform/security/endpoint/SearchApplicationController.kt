package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.SearchApplicationDelegate
import com.wutsi.platform.security.dto.SearchApplicationResponse
import org.springframework.web.bind.`annotation`.CrossOrigin
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.RequestParam
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Int
import kotlin.String

@RestController
@CrossOrigin(
    origins = ["*"],
    allowedHeaders = ["Content-Type", "Authorization", "Content-Length", "X-Requested-With"],
    methods = [
        org.springframework.web.bind.annotation.RequestMethod.GET,
        org.springframework.web.bind.annotation.RequestMethod.DELETE,
        org.springframework.web.bind.annotation.RequestMethod.OPTIONS,
        org.springframework.web.bind.annotation.RequestMethod.HEAD,
        org.springframework.web.bind.annotation.RequestMethod.POST,
        org.springframework.web.bind.annotation.RequestMethod.PUT
    ]
)
public class SearchApplicationController(
    private val `delegate`: SearchApplicationDelegate
) {
    @GetMapping("/v1/applications")
    public fun invoke(
        @RequestParam(name = "name", required = false) name: String? = null,
        @RequestParam(name = "limit", required = false, defaultValue = "20") limit: Int = 20,
        @RequestParam(name = "offset", required = false, defaultValue = "0") offset: Int = 0
    ): SearchApplicationResponse = delegate.invoke(name, limit, offset)
}
