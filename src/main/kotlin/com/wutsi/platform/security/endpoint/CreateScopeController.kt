package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.CreateScopeDelegate
import com.wutsi.platform.security.dto.CreateScopeRequest
import com.wutsi.platform.security.dto.CreateScopeResponse
import org.springframework.web.bind.`annotation`.CrossOrigin
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

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
public class CreateScopeController(
    private val `delegate`: CreateScopeDelegate
) {
    @PostMapping("/v1/scopes")
    public fun invoke(@Valid @RequestBody request: CreateScopeRequest): CreateScopeResponse =
        delegate.invoke(request)
}
