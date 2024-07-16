package com.teamkkumul.core.data.repository

import java.io.File

interface ProfileRepository {
    suspend fun updateImage(file: File): Result<Unit>
}
