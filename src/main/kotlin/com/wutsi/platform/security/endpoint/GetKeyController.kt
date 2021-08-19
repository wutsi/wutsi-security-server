package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.GetKeyDelegate
import com.wutsi.platform.security.dto.GetKeyResponse
import org.springframework.web.bind.`annotation`.CrossOrigin
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
public class GetKeyController(
    private val `delegate`: GetKeyDelegate
) {
    @GetMapping("/v1/keys/{id}")
    public fun invoke(@PathVariable id: Long): GetKeyResponse = delegate.invoke(id)
}
