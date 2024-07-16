package com.teamkkumul.core.data.repository

import com.teamkkumul.model.login.LoginModel

interface LoginRepository {
    suspend fun postLogin(socialType: String, fcmToken: String, header: String): Result<LoginModel>
}
