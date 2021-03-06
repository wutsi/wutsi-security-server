package com.wutsi.platform.security.service.connector

import com.wutsi.platform.account.WutsiAccountApi
import com.wutsi.platform.account.dto.SearchAccountRequest
import com.wutsi.platform.security.dao.ScopeRepository
import com.wutsi.platform.security.dto.AuthenticationRequest
import org.springframework.stereotype.Service

@Service
class WutsiConnector(
    private val accountApi: WutsiAccountApi,
    private val scopeDao: ScopeRepository
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
            "mail-send",
            "sms-verify",
            "contact-read",
            "contact-manage",
            "qr-read",
            "qr-manage",
            "catalog-manage",
            "catalog-read",
            "cart-read",
            "cart-manage",
            "order-read",
            "order-manage",
            "shipping-read",
            "shipping-manage",
        ).sorted()
    }

    override fun authenticate(request: AuthenticationRequest): User? {
        val accounts = accountApi.searchAccount(
            SearchAccountRequest(
                phoneNumber = request.phoneNumber
            )
        ).accounts

        if (accounts.isEmpty())
            return null

        val scopes = if (accounts[0].superUser)
            scopeDao.findAll()
                .filter { it.active }
                .map { it.name }
        else
            SCOPES

        return User(
            id = accounts[0].id,
            displayName = accounts[0].displayName,
            active = "active".equals(accounts[0].status, true),
            language = accounts[0].language,
            scopes = scopes,
            admin = accounts[0].superUser,
        )
    }
}
