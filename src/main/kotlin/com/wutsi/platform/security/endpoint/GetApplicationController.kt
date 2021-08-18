package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.GetApplicationDelegate
import com.wutsi.platform.security.dto.GetApplicationResponse
import org.springframework.web.bind.`annotation`.CrossOrigin
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController
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
public class GetApplicationController(
    private val `delegate`: GetApplicationDelegate
) {
    @GetMapping("/v1/applications/{id}")
    public fun invoke(@PathVariable(name = "id") @NotNull id: Long): GetApplicationResponse =
        delegate.invoke(id)
}
