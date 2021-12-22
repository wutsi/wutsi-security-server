package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.GetApplicationDelegate
import com.wutsi.platform.security.dto.GetApplicationResponse
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Long

@RestController
public class GetApplicationController(
    private val `delegate`: GetApplicationDelegate
) {
    @GetMapping("/v1/applications/{id}")
    public fun invoke(@PathVariable(name = "id") id: Long): GetApplicationResponse = delegate.invoke(id)
}
