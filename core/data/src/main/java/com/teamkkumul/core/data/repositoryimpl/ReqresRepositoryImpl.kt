package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toReqresModel
import com.teamkkumul.core.data.repository.ReqresRepository
import com.teamkkumul.core.network.api.ReqresService
import com.teamkkumul.model.example.ReqresModel
import javax.inject.Inject

internal class ReqresRepositoryImpl @Inject constructor(
    private val reqresService: ReqresService,
) : ReqresRepository {
    override suspend fun getReqresList(page: Int): Result<List<ReqresModel>> = runCatching {
        reqresService.getUserList(page).toReqresModel()
    }
}
