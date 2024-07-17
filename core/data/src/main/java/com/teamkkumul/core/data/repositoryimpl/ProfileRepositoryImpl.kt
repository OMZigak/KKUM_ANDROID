package com.teamkkumul.core.data.repositoryimpl

import android.util.Log
import com.teamkkumul.core.data.repository.ProfileRepository
import com.teamkkumul.core.network.api.ProfileService
import com.teamkkumul.core.network.dto.request.RequestNameDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

internal class ProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService,
) : ProfileRepository {
    override suspend fun updateName(request: String): Result<String> =
        runCatching {
            profileService.updateName(RequestNameDto(request)).data.name
        }

    override suspend fun updateImage(file: File): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
                val response = profileService.updateImage(body)
                if (response.success) {
                    Log.e("Profile Image", "프로필 이미지 업로드 성공")
                    Result.success(Unit)
                } else {
                    Log.e("Profile Image", "프로필 이미지 업로드 실패")
                    Result.failure(Throwable(response.error?.message))
                }
            } catch (e: Exception) {
                Log.e("Profile Image", "프로필 이미지 업로드 예외 발생: ${e.message}")
                Result.failure(e)
            }
        }
    }
}
