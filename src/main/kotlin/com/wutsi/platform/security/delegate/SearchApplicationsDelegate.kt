package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.security.dto.SearchApplicationResponse
import com.wutsi.platform.security.entity.ApplicationEntity
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.Query

@Service
public class SearchApplicationsDelegate(
    private val mapper: Mapper,
    private val em: EntityManager
) {
    public fun invoke(
        name: String? = null,
        limit: Int = 20,
        offset: Int = 0
    ): SearchApplicationResponse {
        val query = em.createQuery(sql(name))
        parameters(name, query)
        val applications = query
            .setFirstResult(offset)
            .setMaxResults(limit)
            .resultList as List<ApplicationEntity>

        return SearchApplicationResponse(
            applications = applications.map { mapper.toApplicationSummary(it) }
        )
    }

    private fun sql(name: String?): String {
        val select = select()
        val where = where(name)

        return if (where.isNullOrEmpty())
            select
        else
            "$select WHERE $where"
    }

    private fun select(): String =
        "SELECT a FROM ApplicationEntity a"

    private fun where(name: String?): String {
        val criteria = mutableListOf<String>()
        if (!name.isNullOrEmpty())
            criteria.add("a.name=:name")
        return criteria.joinToString(separator = " AND ")
    }

    private fun parameters(name: String?, query: Query) {
        if (!name.isNullOrEmpty())
            query.setParameter("name", name)
    }
}
