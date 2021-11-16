package com.wutsi.platform.security.service.connector

import com.wutsi.platform.account.WutsiAccountApi
import com.wutsi.platform.security.dto.AuthenticationRequest
import org.springframework.stereotype.Service

@Service
class WutsiConnector(
    private val accountApi: WutsiAccountApi
) : Connector {
    companion object {
        val SCOPES = listOf(
            "user-read",
            "user-manage",
            "payment-method-manage",
            "payment-method-read",
            "payment-manage",
            "payment-read",
            "tenant-read",
            "payment-manage",
            "payment-read",
            "sms-send",
            "sms-verify"
        ).sorted()
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
            active = "active".equals(accounts[0].status, true),
            language = accounts[0].language,
            scopes = SCOPES,
            admin = accounts[0].superUser,
        )
    }
}
