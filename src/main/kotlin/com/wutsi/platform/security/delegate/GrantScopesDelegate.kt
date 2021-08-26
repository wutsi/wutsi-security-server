package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_PATH
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_PAYLOAD
import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.core.error.exception.NotFoundException
import com.wutsi.platform.security.dao.ApplicationRepository
import com.wutsi.platform.security.dao.ScopeRepository
import com.wutsi.platform.security.dto.GrantScopeRequest
import com.wutsi.platform.security.entity.ScopeEntity
import com.wutsi.platform.security.util.ErrorURN
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
public class GrantScopesDelegate(
    private val applicationDao: ApplicationRepository,
    private val dao: ScopeRepository
) {
    @Transactional
    public fun invoke(id: Long, request: GrantScopeRequest) {
        val app = applicationDao.findById(id)
            .orElseThrow {
                NotFoundException(
                    Error(
                        code = ErrorURN.APPLICATION_NOT_FOUND.urn,
                        parameter = Parameter(
                            name = "id",
                            value = id,
                            type = PARAMETER_TYPE_PATH
                        )
                    )
                )
            }

        val scopes: List<ScopeEntity> = request.scopeNames
            .map {
                dao.findByNameIgnoreCase(it)
                    .orElseThrow {
                        BadRequestException(
                            Error(
                                code = ErrorURN.INVALID_SCOPE.urn,
                                parameter = Parameter(
                                    name = "scopeNames",
                                    value = it,
                                    type = PARAMETER_TYPE_PAYLOAD
                                )
                            )
                        )
                    }
            }

        val scopeToAdd = app.scopes.filter { !scopes.contains(it) }
        app.scopes.addAll(scopeToAdd)
        applicationDao.save(app)
    }
}
