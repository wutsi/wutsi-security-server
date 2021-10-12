package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.SearchScopesDelegate
import com.wutsi.platform.security.dto.SearchScopeResponse
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class SearchScopesController(
    private val `delegate`: SearchScopesDelegate
) {
    @GetMapping("/v1/scopes")
    public fun invoke(): SearchScopeResponse = delegate.invoke()
}
