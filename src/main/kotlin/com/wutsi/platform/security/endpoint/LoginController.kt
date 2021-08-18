package com.wutsi.platform.security.endpoint

import com.wutsi.platform.security.`delegate`.LoginDelegate
import com.wutsi.platform.security.dto.LoginRequest
import com.wutsi.platform.security.dto.LoginResponse
import org.springframework.web.bind.`annotation`.CrossOrigin
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

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
public class LoginController(
    private val `delegate`: LoginDelegate
) {
    @PostMapping("/v1/login")
    public fun invoke(@Valid @RequestBody request: LoginRequest): LoginResponse =
        delegate.invoke(request)
}
