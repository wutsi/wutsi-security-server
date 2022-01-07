package com.wutsi.platform.security.`delegate`

import com.wutsi.platform.security.dao.LoginRepository
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

@Service
public class LogoutDelegate(
    private val request: HttpServletRequest,
    private val dao: LoginRepository
) {
    @Transactional
    public fun invoke() {
        val accessToken = getAccessToken()
            ?: return

        val login = dao.findByAccessToken(accessToken).orElse(null)
            ?: return

        login.active = false
        dao.save(login)
    }

    protected fun getAccessToken(): String? {
        val request = request
        val value = request.getHeader("Authorization") ?: return null
        return if (value.startsWith("Bearer ", ignoreCase = true))
            value.substring(7)
        else
            null
    }
}
