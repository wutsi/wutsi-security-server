package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_PAYLOAD
import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.security.dao.ApplicationRepository
import com.wutsi.platform.security.dao.ScopeRepository
import com.wutsi.platform.security.dto.CreateApplicationRequest
import com.wutsi.platform.security.dto.CreateApplicationResponse
import com.wutsi.platform.security.entity.ApplicationEntity
import com.wutsi.platform.security.entity.ScopeEntity
import com.wutsi.platform.security.util.ErrorURN
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.UUID
import javax.transaction.Transactional

@Service
public class CreateApplicationDelegate(
    private val dao: ApplicationRepository,
    private val scopeDao: ScopeRepository
) {
    @Transactional
    public fun invoke(request: CreateApplicationRequest): CreateApplicationResponse {
        try {
            val app = dao.save(
                ApplicationEntity(
                    apiKey = UUID.randomUUID().toString(),
                    name = request.name,
                    title = request.title,
                    description = request.description,
                    securityLevel = request.securityLevel,
                    homeUrl = request.homeUrl,
                    configUrl = request.configUrl,
                    scopes = toScopes(request),
                    created = OffsetDateTime.now()
                )
            )
            return CreateApplicationResponse(
                id = app.id ?: -1,
                apiKey = app.apiKey
            )
        } catch (ex: DataIntegrityViolationException) {
            throw ConflictException(
                error = Error(
                    code = ErrorURN.APPLICATION_ALREADY_EXIST.urn.toString(),
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

    private fun toScopes(request: CreateApplicationRequest): List<ScopeEntity> {
        val scopes = mutableListOf<ScopeEntity>()
        request.scopeNames.forEach {
            val scope = scopeDao.findByNameIgnoreCase(it)
                .orElseThrow {
                    BadRequestException(
                        error = Error(
                            code = ErrorURN.INVALID_SCOPE.urn.toString(),
                            parameter = Parameter(
                                name = "scopeNames",
                                type = PARAMETER_TYPE_PAYLOAD,
                                value = it
                            )
                        )
                    )
                }
            scopes.add(scope)
        }
        return scopes
    }
}
