package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.api.util.KeyStorage.API
import com.teamkkumul.core.network.api.util.KeyStorage.IMAGE
import com.teamkkumul.core.network.api.util.KeyStorage.ME
import com.teamkkumul.core.network.api.util.KeyStorage.NAME
import com.teamkkumul.core.network.api.util.KeyStorage.USERS
import com.teamkkumul.core.network.api.util.KeyStorage.V1
import com.teamkkumul.core.network.dto.request.RequestSetNameDto
import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseSetNameDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part

interface ProfileService {

    @PATCH("/$API/$V1/$USERS/$ME/$NAME")
    suspend fun updateName(
        @Body request: RequestSetNameDto,
    ): BaseResponse<ResponseSetNameDto>

    @Multipart
    @PATCH("/$API/$V1/$USERS/$ME/$IMAGE")
    suspend fun updateImage(
        @Part image: MultipartBody.Part?,
    ): BaseResponse<Unit>
}
