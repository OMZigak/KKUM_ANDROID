package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseMyGroupDto
import retrofit2.http.GET

interface MyGroupService {
    @GET("/api/v1/meetings")
    suspend fun getMyGroupList(): BaseResponse<ResponseMyGroupDto>
}
