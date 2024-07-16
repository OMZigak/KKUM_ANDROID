package com.teamkkumul.core.data.repository

import java.io.File

interface ProfileRepository {
    suspend fun updateName(request: String): Result<String>
    suspend fun updateImage(file: File): Result<Unit>
}
