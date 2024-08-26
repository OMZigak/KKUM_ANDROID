package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toLoginModel
import com.teamkkumul.core.data.repository.LoginRepository
import com.teamkkumul.core.data.utils.handleThrowable
import com.teamkkumul.core.network.api.LoginService
import com.teamkkumul.core.network.api.LogoutService
import com.teamkkumul.core.network.dto.request.RequestLoginDto
import com.teamkkumul.model.login.LoginModel
import javax.inject.Inject

internal class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService,
    private val logoutService: LogoutService,
) : LoginRepository {
    override suspend fun postLogin(
        socialType: String,
        fcmToken: String,
        header: String,
    ): Result<LoginModel> = runCatching {
        loginService.postLogin(
            RequestLoginDto(socialType, fcmToken),
            header,
        ).data?.toLoginModel()
    }.mapCatching {
        requireNotNull(it)
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun postLogout(): Result<Unit> = runCatching {
        logoutService.postLogout()
        Unit
    }.onFailure { return it.handleThrowable() }

    override suspend fun deleteWithdrawal(): Result<Unit> = runCatching {
        logoutService.deleteWithdrawal()
        Unit
    }.onFailure { return it.handleThrowable() }
}
