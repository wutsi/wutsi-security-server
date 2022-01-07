package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.LogoutDelegate
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class LogoutController(
    private val `delegate`: LogoutDelegate
) {
    @GetMapping("/v1/logout")
    public fun invoke() {
        delegate.invoke()
    }
}
