package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toMyGroupModel
import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.core.network.api.MyGroupService
import com.teamkkumul.model.MyGroupModel
import javax.inject.Inject

class MyGroupRepositoryImpl @Inject constructor(
    private val myGroupService: MyGroupService,
) : MyGroupRepository {
    override suspend fun getMyGroup(): Result<MyGroupModel> = runCatching {
        val response = myGroupService.getMyGroupList()
        response.data.toMyGroupModel()
    }
}
