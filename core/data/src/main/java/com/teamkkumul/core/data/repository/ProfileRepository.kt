package com.teamkkumul.core.data.repository

interface ProfileRepository {
    suspend fun updateName(request: String): Result<String>
    suspend fun updateImage(uriString: String?): Result<Boolean>
}
