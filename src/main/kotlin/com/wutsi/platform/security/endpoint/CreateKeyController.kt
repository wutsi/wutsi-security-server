package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.CreateKeyDelegate
import com.wutsi.platform.security.dto.CreateKeyResponse
import org.springframework.web.bind.`annotation`.CrossOrigin
import org.springframework.web.bind.`annotation`.PostMapping
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
public class CreateKeyController(
    private val `delegate`: CreateKeyDelegate
) {
    @PostMapping("/v1/keys")
    public fun invoke(): CreateKeyResponse = delegate.invoke()
}
