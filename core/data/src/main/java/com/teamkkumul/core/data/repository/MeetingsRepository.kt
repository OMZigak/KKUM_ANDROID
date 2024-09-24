package com.teamkkumul.core.data.repository

import com.teamkkumul.model.AddNewGroupModel

interface MeetingsRepository {
    suspend fun addNewGroup(request: String): Result<AddNewGroupModel>
    suspend fun enterInvitationCode(request: String): Result<Int>
}
