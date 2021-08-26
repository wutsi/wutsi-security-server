package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_PAYLOAD
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.security.dao.ScopeRepository
import com.wutsi.platform.security.dto.CreateScopeRequest
import com.wutsi.platform.security.dto.CreateScopeResponse
import com.wutsi.platform.security.entity.ScopeEntity
import com.wutsi.platform.security.util.ErrorURN
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
public class CreateScopeDelegate(private val dao: ScopeRepository) {
    @Transactional
    public fun invoke(request: CreateScopeRequest): CreateScopeResponse {
        try {
            val scope = dao.save(
                ScopeEntity(
                    name = request.name.toLowerCase(),
                    description = request.description,
                    securityLevel = request.securityLevel,
                    active = true
                )
            )

            return CreateScopeResponse(
                id = scope.id ?: -1
            )
        } catch (ex: DataIntegrityViolationException) {
            throw ConflictException(
                error = Error(
                    code = ErrorURN.SCOPE_ALREADY_EXIST.urn.toString(),
                    parameter = Parameter(
                        name = "name",
                        type = PARAMETER_TYPE_PAYLOAD,
                        value = request.name
                    )
                ),
                ex
            )
        }
    }
}
