package com.wutsi.platform.security.service.auth

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType.PARAMETER_TYPE_PAYLOAD
import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.core.error.exception.ForbiddenException
import com.wutsi.platform.security.dto.AuthenticationRequest
import com.wutsi.platform.security.entity.LoginEntity
import com.wutsi.platform.security.entity.MFALoginType
import com.wutsi.platform.security.service.LoginService
import com.wutsi.platform.security.service.connector.WutsiConnector
import com.wutsi.platform.security.util.ErrorURN
import com.wutsi.platform.sms.WutsiSmsApi
import com.wutsi.platform.sms.dto.SendVerificationRequest
import feign.FeignException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
public class SMSAuthenticator(
    private val connector: WutsiConnector,
    private val smsApi: WutsiSmsApi,
    private val mfaService: MFAService,
    private val loginService: LoginService,
) : Authenticator {
    override fun validate(request: AuthenticationRequest) {
        if (request.mfaToken.isNotEmpty()) {
            if (request.verificationCode.isEmpty()) {
                throw BadRequestException(
                    error = Error(
                        code = ErrorURN.VERIFICATION_CODE_MISSING.urn,
                        parameter = Parameter(
                            name = "verificationCode",
                            type = PARAMETER_TYPE_PAYLOAD
                        )
                    )
                )
            }
        } else if (request.phoneNumber.isEmpty())
            throw BadRequestException(
                error = Error(
                    code = ErrorURN.PHONE_NUMBER_REQUIRED.urn,
                    parameter = Parameter(
                        name = "phoneNumber",
                        type = PARAMETER_TYPE_PAYLOAD
                    )
                )
            )
    }

    override fun authenticate(request: AuthenticationRequest): LoginEntity {
        if (request.mfaToken.isNotEmpty())
            return verify(request)
        else
            return send(request)
    }

    private fun send(request: AuthenticationRequest): LoginEntity {
        val user = connector.authenticate(request)
        if (user == null)
            throw ConflictException(error = Error(code = ErrorURN.USER_NOT_FOUND.urn))
        else if (!user.active)
            throw ConflictException(error = Error(code = ErrorURN.USER_NOT_ACTIVE.urn))

        val verificationId = smsApi.sendVerification(
            request = SendVerificationRequest(
                phoneNumber = request.phoneNumber
            )
        ).id
        val token = UUID.randomUUID().toString()
        mfaService.saveAsync(MFALoginType.MFA_LOGIN_TYPE_SMS, user, token, verificationId)

        throw ForbiddenException(
            error = Error(
                code = ErrorURN.MFA_REQUIRED.urn,
                data = mapOf(
                    "mfaToken" to token
                )
            )
        )
    }

    private fun verify(request: AuthenticationRequest): LoginEntity {
        val mfa = mfaService.findByToken(request.mfaToken)
            .orElseThrow {
                ConflictException(
                    error = Error(
                        code = ErrorURN.MFA_INVALID_TOKEN.urn,
                        parameter = Parameter(
                            name = "mfaToken",
                            value = request.mfaToken,
                            type = PARAMETER_TYPE_PAYLOAD
                        )
                    )
                )
            }

        try {
            smsApi.validateVerification(mfa.verificationId, request.verificationCode)
            return loginService.login(mfa)
        } catch (ex: FeignException) {
            if (ex.status() == 409) {
                throw ConflictException(
                    error = Error(code = ErrorURN.MFA_VERIFICATION_FAILED.urn),
                    ex = ex
                )
            } else {
                throw ex
            }
        }
    }
}
