package com.wutsi.platform.security.delegate

import com.wutsi.platform.security.dto.Application
import com.wutsi.platform.security.dto.Scope
import com.wutsi.platform.security.entity.ApplicationEntity
import com.wutsi.platform.security.entity.ScopeEntity
import org.springframework.stereotype.Service

@Service
class Mapper {
    fun toScope(obj: ScopeEntity): Scope =
        Scope(
            id = obj.id ?: -1,
            name = obj.name,
            description = obj.description,
            securityLevel = obj.securityLevel,
            active = obj.active
        )

    fun toApplication(app: ApplicationEntity): Application =
        Application(
            id = app.id ?: -1,
            name = app.name,
            title = app.title,
            apiKey = app.apiKey,
            description = app.description,
            configUrl = app.configUrl,
            homeUrl = app.homeUrl,
            securityLevel = app.securityLevel,
            active = app.active,
            scopes = app.scopes
                .filter { it.active }
                .map { toScope(it) }
                .sortedBy { it.name }
        )
}
