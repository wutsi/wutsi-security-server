package com.wutsi.platform.security.service.connector

import com.wutsi.platform.security.dto.AuthenticationRequest

interface Connector {
    fun authenticate(request: AuthenticationRequest): User?
}
