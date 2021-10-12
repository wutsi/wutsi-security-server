package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.AuthenticateDelegate
import com.wutsi.platform.security.dto.AuthenticationRequest
import com.wutsi.platform.security.dto.AuthenticationResponse
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class AuthenticateController(
    private val `delegate`: AuthenticateDelegate
) {
    @PostMapping("/v1/auth")
    public fun invoke(@Valid @RequestBody request: AuthenticationRequest): AuthenticationResponse =
        delegate.invoke(request)
}
