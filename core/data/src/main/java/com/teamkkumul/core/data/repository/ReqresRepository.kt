package com.teamkkumul.core.data.repository

import com.teamkkumul.model.example.ReqresModel

interface ReqresRepository {
    suspend fun getReqresList(page: Int): Result<List<ReqresModel>>
}
