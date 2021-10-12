package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.ApplicationDelegate
import com.wutsi.platform.security.dto.GetApplicationResponse
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.RequestHeader
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.constraints.NotBlank
import kotlin.String

@RestController
public class ApplicationController(
    private val `delegate`: ApplicationDelegate
) {
    @GetMapping("/v1/applications/me")
    public fun invoke(@RequestHeader(name = "X-Api-Key", required = true) @NotBlank xApiKey: String):
        GetApplicationResponse = delegate.invoke(xApiKey)
}
