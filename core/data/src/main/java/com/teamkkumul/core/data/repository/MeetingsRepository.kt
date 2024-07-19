package com.teamkkumul.core.data.repository

interface MeetingsRepository {
    suspend fun addNewGroup(request: String): Result<String>
    suspend fun enterInvitationCode(request: String): Result<Int>
}
