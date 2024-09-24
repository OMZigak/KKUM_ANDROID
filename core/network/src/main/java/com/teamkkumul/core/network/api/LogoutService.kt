package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.dto.response.BaseResponse
import retrofit2.http.DELETE
import retrofit2.http.POST

interface LogoutService {
    @POST("api/v1/auth/signout")
    suspend fun postLogout(): BaseResponse<Unit>

    @DELETE("api/v1/auth/withdrawal")
    suspend fun deleteWithdrawal(): BaseResponse<Unit>
}
