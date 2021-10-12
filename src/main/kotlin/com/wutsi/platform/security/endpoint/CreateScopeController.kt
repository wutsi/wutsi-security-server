package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.CreateScopeDelegate
import com.wutsi.platform.security.dto.CreateScopeRequest
import com.wutsi.platform.security.dto.CreateScopeResponse
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class CreateScopeController(
    private val `delegate`: CreateScopeDelegate
) {
    @PostMapping("/v1/scopes")
    public fun invoke(@Valid @RequestBody request: CreateScopeRequest): CreateScopeResponse =
        delegate.invoke(request)
}
