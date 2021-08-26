package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.GrantScopesDelegate
import com.wutsi.platform.security.dto.GrantScopeRequest
import org.springframework.web.bind.`annotation`.CrossOrigin
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import javax.validation.constraints.NotNull
import kotlin.Long

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
public class GrantScopesController(
    private val `delegate`: GrantScopesDelegate
) {
    @PostMapping("/v1/applications/{id}/scopes")
    public fun invoke(
        @PathVariable(name = "id") @NotNull id: Long,
        @Valid @RequestBody
        request: GrantScopeRequest
    ) {
        delegate.invoke(id, request)
    }
}
