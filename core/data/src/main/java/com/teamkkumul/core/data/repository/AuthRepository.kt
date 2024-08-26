package com.teamkkumul.core.data.repository

import com.teamkkumul.model.login.LoginModel

interface AuthRepository {
    suspend fun postLogin(socialType: String, fcmToken: String, header: String): Result<LoginModel>

    suspend fun postLogout(): Result<Unit>

    suspend fun deleteWithdrawal(): Result<Unit>
}
