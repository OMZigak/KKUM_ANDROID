package com.teamkkumul.core.data.repositoryimpl

import android.content.ContentResolver
import com.teamkkumul.core.data.repository.ProfileRepository
import com.teamkkumul.core.data.utils.createImagePart
import com.teamkkumul.core.data.utils.handleThrowable
import com.teamkkumul.core.network.api.ProfileService
import com.teamkkumul.core.network.dto.request.RequestSetNameDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ProfileRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    private val profileService: ProfileService,
) : ProfileRepository {
    override suspend fun updateName(request: String): Result<String> = runCatching {
        profileService.updateName(RequestSetNameDto(request)).data?.name
    }.mapCatching {
        requireNotNull(it)
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun updateImage(uriString: String?): Result<Boolean> = runCatching {
        val imagePart = withContext(Dispatchers.IO) {
            createImagePart(contentResolver, uriString)
        }
        profileService.updateImage(imagePart).success
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun deleteImage(): Result<Unit> = runCatching {
        profileService.deleteImage().success
        Unit
    }.recoverCatching {
        return it.handleThrowable()
    }
}
