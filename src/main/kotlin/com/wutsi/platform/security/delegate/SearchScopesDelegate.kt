package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.security.dao.ScopeRepository
import com.wutsi.platform.security.dto.SearchScopeResponse
import org.springframework.stereotype.Service

@Service
public class SearchScopesDelegate(
    private val dao: ScopeRepository,
    private val mapper: Mapper
) {
    public fun invoke(): SearchScopeResponse {
        val scopes = dao.findAll()
        return SearchScopeResponse(
            scopes = scopes.map {
                mapper.toScope(it)
            }.sortedBy { it.name }
        )
    }
}
