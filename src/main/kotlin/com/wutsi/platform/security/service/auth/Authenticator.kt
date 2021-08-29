package com.wutsi.platform.security.service.auth

import com.wutsi.platform.security.dto.AuthenticationRequest
import com.wutsi.platform.security.dto.AuthenticationResponse

interface Authenticator {
    fun validate(request: AuthenticationRequest)

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse
}
