package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.CreateApplicationDelegate
import com.wutsi.platform.security.dto.CreateApplicationRequest
import com.wutsi.platform.security.dto.CreateApplicationResponse
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class CreateApplicationController(
    private val `delegate`: CreateApplicationDelegate
) {
    @PostMapping("/v1/applications/me")
    public fun invoke(@Valid @RequestBody request: CreateApplicationRequest):
        CreateApplicationResponse = delegate.invoke(request)
}
