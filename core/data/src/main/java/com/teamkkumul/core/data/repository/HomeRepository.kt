package com.teamkkumul.core.data.repository

import com.teamkkumul.model.home.HomeTodayMeetingModel
import com.teamkkumul.model.home.UserModel

interface HomeRepository {
    suspend fun getUserInfo(): Result<UserModel>

    suspend fun getTodayMeeting(): Result<HomeTodayMeetingModel?>

    suspend fun getUpComingMeeting(): Result<List<HomeTodayMeetingModel>>
}
