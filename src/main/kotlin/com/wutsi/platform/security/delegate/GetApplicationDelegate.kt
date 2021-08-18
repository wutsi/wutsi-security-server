package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_PATH
import com.wutsi.platform.core.error.exception.NotFoundException
import com.wutsi.platform.security.dao.ApplicationRepository
import com.wutsi.platform.security.dto.GetApplicationResponse
import com.wutsi.platform.security.util.ErrorURN.APPLICATION_NOT_FOUND
import org.springframework.stereotype.Service

@Service
public class GetApplicationDelegate(
    private val dao: ApplicationRepository,
    private val mapper: Mapper
) {
    public fun invoke(id: Long): GetApplicationResponse {
        val app = dao.findById(id).orElseThrow {
            NotFoundException(
                error = Error(
                    code = APPLICATION_NOT_FOUND.urn,
                    parameter = Parameter(
                        name = "id",
                        type = PARAMETER_TYPE_PATH,
                        value = id
                    )
                )
            )
        }
        return GetApplicationResponse(
            application = mapper.toApplication(app)
        )
    }
}
