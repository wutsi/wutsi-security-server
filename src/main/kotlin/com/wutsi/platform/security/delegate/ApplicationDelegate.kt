package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_HEADER
import com.wutsi.platform.core.error.exception.NotFoundException
import com.wutsi.platform.security.dao.ApplicationRepository
import com.wutsi.platform.security.dto.GetApplicationResponse
import com.wutsi.platform.security.util.ErrorURN.APPLICATION_NOT_FOUND
import org.springframework.stereotype.Service

@Service
public class ApplicationDelegate(
    private val dao: ApplicationRepository,
    private val mapper: Mapper
) {
    public fun invoke(xApiKey: String): GetApplicationResponse {
        val app = dao.findByApiKey(xApiKey).orElseThrow {
            NotFoundException(
                error = Error(
                    code = APPLICATION_NOT_FOUND.urn,
                    parameter = Parameter(
                        name = "X-Api-Key",
                        type = PARAMETER_TYPE_HEADER,
                        value = xApiKey
                    )
                )
            )
        }
        return GetApplicationResponse(
            application = mapper.toApplication(app)
        )
    }
}
