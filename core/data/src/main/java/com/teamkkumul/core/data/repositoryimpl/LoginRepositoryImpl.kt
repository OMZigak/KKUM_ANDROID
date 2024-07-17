package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toLoginModel
import com.teamkkumul.core.data.repository.LoginRepository
import com.teamkkumul.core.network.api.LoginService
import com.teamkkumul.core.network.dto.request.RequestLoginDto
import com.teamkkumul.model.login.LoginModel
import javax.inject.Inject

internal class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService,
) : LoginRepository {
    override suspend fun postLogin(
        socialType: String,
        fcmToken: String,
        header: String,
    ): Result<LoginModel> =
        runCatching {
            loginService.postLogin(
                RequestLoginDto(socialType, fcmToken),
                header,
            ).data?.toLoginModel() ?: throw Exception("null")
        }.onFailure { throwable ->
            return Result.failure(Exception(throwable.message))
        }
}
