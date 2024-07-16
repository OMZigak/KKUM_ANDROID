package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.repository.ProfileRepository
import com.teamkkumul.core.network.api.ProfileService
import java.io.File
import javax.inject.Inject

internal class ProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService,
) : ProfileRepository {
    override suspend fun updateImage(file: File): Result<Unit> {

    }
}