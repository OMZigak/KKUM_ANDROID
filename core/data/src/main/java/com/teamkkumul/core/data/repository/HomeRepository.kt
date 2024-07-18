package com.teamkkumul.core.data.repository

import com.teamkkumul.model.home.HomeMembersStatus
import com.teamkkumul.model.home.HomeReadyStatusModel
import com.teamkkumul.model.home.HomeTodayMeetingModel
import com.teamkkumul.model.home.UserModel

interface HomeRepository {
    suspend fun getUserInfo(): Result<UserModel>

    suspend fun getTodayMeeting(): Result<HomeTodayMeetingModel?>

    suspend fun getUpComingMeeting(): Result<List<HomeTodayMeetingModel>>
    suspend fun getReadyStatus(promiseId: Int): Result<HomeReadyStatusModel?>
    suspend fun patchReady(promiseId: Int): Result<Unit>
    suspend fun patchMoving(promiseId: Int): Result<Unit>
    suspend fun patchCompleted(promiseId: Int): Result<Unit>
    suspend fun getMembersReadyStatus(promiseId: Int): Result<List<HomeMembersStatus.Participant?>>
}
