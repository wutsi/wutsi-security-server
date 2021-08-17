package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.security.dao.ScopeRepository
import com.wutsi.platform.security.dto.Scope
import com.wutsi.platform.security.dto.SearchScopeResponse
import org.springframework.stereotype.Service

@Service
public class SearchScopesDelegate(private val dao: ScopeRepository) {
    public fun invoke(): SearchScopeResponse {
        val scopes = dao.findAll()
        return SearchScopeResponse(
            scopes = scopes.map {
                Scope(
                    id = it.id ?: -1,
                    name = it.name,
                    description = it.description,
                    securityLevel = it.securityLevel,
                    active = it.active
                )
            }.sortedBy { it.name }
        )
    }
}
