package com.teamkkumul.core.data.repositoryimpl

import android.content.ContentResolver
import com.teamkkumul.core.data.repository.ProfileRepository
import com.teamkkumul.core.data.utils.createImagePart
import com.teamkkumul.core.network.api.ProfileService
import com.teamkkumul.core.network.dto.request.RequestNameDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class ProfileRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    private val profileService: ProfileService,
) : ProfileRepository {
    override suspend fun updateName(request: String): Result<String> =
        runCatching {
            profileService.updateName(RequestNameDto(request)).data?.name ?: throw Exception("Data is null")
        }

    override suspend fun updateImage(content: String, uriString: String?): Result<Boolean> {
        return runCatching {
            val imagePart = withContext(Dispatchers.IO) {
                createImagePart(contentResolver, uriString)
            }
            profileService.updateImage(imagePart).success
        }.onFailure { throwable ->
            return when (throwable) {
                is HttpException -> Result.failure(IOException(throwable.message))
                else -> Result.failure(throwable)
            }
        }
    }
}
