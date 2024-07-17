package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toTodayMeetingModel
import com.teamkkumul.core.data.mapper.toUserModel
import com.teamkkumul.core.data.repository.HomeRepository
import com.teamkkumul.core.network.api.HomeService
import com.teamkkumul.model.HomeTodayMeetingModel
import com.teamkkumul.model.home.UserModel
import javax.inject.Inject

internal class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService,
) : HomeRepository {
    override suspend fun getUserInfo(): Result<UserModel> = runCatching {
        homeService.getUserInfo().data?.toUserModel() ?: throw Exception("null")
    }

    override suspend fun getTodayMeeting(): Result<HomeTodayMeetingModel?> = runCatching {
        homeService.getTodayMeeting().data?.toTodayMeetingModel()
    }
}
