package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.GrantScopesDelegate
import com.wutsi.platform.security.dto.GrantScopeRequest
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import javax.validation.constraints.NotNull
import kotlin.Long

@RestController
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
