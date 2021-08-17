package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.SearchScopesDelegate
import com.wutsi.platform.security.dto.SearchScopeResponse
import org.springframework.web.bind.`annotation`.CrossOrigin
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.RestController

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
public class SearchScopesController(
    private val `delegate`: SearchScopesDelegate
) {
    @GetMapping("/v1/scopes")
    public fun invoke(): SearchScopeResponse = delegate.invoke()
}
