package com.teamkkumul.core.data.repositoryimpl

import android.content.ContentResolver
import com.teamkkumul.core.data.repository.ProfileRepository
import com.teamkkumul.core.data.utils.createImagePart
import com.teamkkumul.core.network.api.ProfileService
import com.teamkkumul.core.network.dto.request.RequestNameDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ProfileRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    private val profileService: ProfileService,
) : ProfileRepository {
    override suspend fun updateName(request: String): Result<String> =
        runCatching {
            profileService.updateName(RequestNameDto(request)).data.name
        }

    override suspend fun updateImage(content: String, uriString: String?): Result<Boolean> {
        TODO("Not yet implemented")
    }

//    override suspend fun updateImage(content: String, uriString: String?): Result<Boolean> {
////        return withContext(Dispatchers.IO) {
////            try {
////                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
////                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
////                val response = profileService.updateImage(body)
////                if (response.success) {
////                    Log.e("Profile Image", "프로필 이미지 업로드 성공")
////                    Result.success(Unit)
////                } else {
////                    Log.e("Profile Image", "프로필 이미지 업로드 실패")
////                    Result.failure(Throwable(response.error?.message))
////                }
////            } catch (e: Exception) {
////                Log.e("Profile Image", "프로필 이미지 업로드 예외 발생: ${e.message}")
////                Result.failure(e)
////            }
////        }
//        return runCatching {
//            val imagePart = withContext(Dispatchers.IO) {
//                createImagePart(contentResolver, uriString)
//            }
//
//            profileService.updateImage(imagePart).success
//        }.onFailure { throwable ->
//            return when (throwable) {
//                Result.failure(throwable)
//            }
//        }
//    }
}
