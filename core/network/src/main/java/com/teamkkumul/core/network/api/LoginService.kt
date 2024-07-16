package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.dto.request.RequestLoginDto
import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseLoginDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    @POST("api/v1/auth/signin")
    suspend fun postLogin(
        @Body requestLogin: RequestLoginDto,
        @Header("Authorization") header: String,
    ): BaseResponse<ResponseLoginDto>
}
