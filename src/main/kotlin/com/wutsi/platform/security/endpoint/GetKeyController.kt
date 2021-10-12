package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.GetKeyDelegate
import com.wutsi.platform.security.dto.GetKeyResponse
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.constraints.NotNull
import kotlin.Long

@RestController
public class GetKeyController(
    private val `delegate`: GetKeyDelegate
) {
    @GetMapping("/v1/keys/{id}")
    public fun invoke(@PathVariable(name = "id") @NotNull id: Long): GetKeyResponse =
        delegate.invoke(id)
}
