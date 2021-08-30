package com.wutsi.platform.security.service.connector

import com.wutsi.platform.account.WutsiAccountApi
import com.wutsi.platform.security.dto.AuthenticationRequest
import org.springframework.stereotype.Service

@Service
class WutsiConnector(
    private val accountApi: WutsiAccountApi
) : Connector {
    companion object {
        private val SCOPES = listOf(
            "user-read",
            "user-manage"
        )
    }

    override fun authenticate(request: AuthenticationRequest): User? {
        val accounts = accountApi.searchAccount(
            phoneNumber = request.phoneNumber,
            limit = 1,
            offset = 0
        ).accounts

        if (accounts.isEmpty())
            return null

        return User(
            id = accounts[0].id,
            displayName = accounts[0].displayName,
            active = accounts[0].status == "active",
            language = accounts[0].language,
            scopes = SCOPES
        )
    }
}