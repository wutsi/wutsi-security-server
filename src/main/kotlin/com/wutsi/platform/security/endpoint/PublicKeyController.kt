package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.PublicKeyDelegate
import com.wutsi.platform.security.dto.PublicKeyResponse
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
public class PublicKeyController(
    private val `delegate`: PublicKeyDelegate
) {
    @GetMapping("/v1/public-key")
    public fun invoke(): PublicKeyResponse = delegate.invoke()
}
