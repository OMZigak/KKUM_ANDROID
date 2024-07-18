package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseMyGroupDto
import com.teamkkumul.core.network.dto.response.ResponseMyGroupInfoDto
import com.teamkkumul.core.network.dto.response.ResponseMyGroupMeetUpDto
import com.teamkkumul.core.network.dto.response.ResponseMyGroupMemberDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyGroupService {
    @GET("/api/v1/meetings")
    suspend fun getMyGroupList(): BaseResponse<ResponseMyGroupDto>

    @GET("/api/v1/meetings/{meetingId}")
    suspend fun getMyGroupInfo(
        @Path("meetingId") meetingId: Int,
    ): BaseResponse<ResponseMyGroupInfoDto>

    @GET("/api/v1/meetings/{meetingId}/members")
    suspend fun getMyGroupMember(
        @Path("meetingId") meetingId: Int,
    ): BaseResponse<ResponseMyGroupMemberDto>

    @GET("/api/v1/meetings/{meetingId}/promises")
    suspend fun getMyGroupMeetUp(
        @Path("meetingId") meetingId: Int,
        @Query("done") done: Boolean,
    ): BaseResponse<ResponseMyGroupMeetUpDto>
}
