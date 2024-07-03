package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.dto.response.ResponseReqresDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ReqresService {
    @GET("api/users")
    suspend fun getUserList(
        @Query("page") page: Int,
    ): ResponseReqresDto
}
