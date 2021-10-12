package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.CreateKeyDelegate
import com.wutsi.platform.security.dto.CreateKeyResponse
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class CreateKeyController(
    private val `delegate`: CreateKeyDelegate
) {
    @PostMapping("/v1/keys")
    public fun invoke(): CreateKeyResponse = delegate.invoke()
}
