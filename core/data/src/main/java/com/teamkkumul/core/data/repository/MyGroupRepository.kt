package com.teamkkumul.core.data.repository

import com.teamkkumul.model.MyGroupModel

interface MyGroupRepository {
    suspend fun getMyGroup(): Result<MyGroupModel>
}
