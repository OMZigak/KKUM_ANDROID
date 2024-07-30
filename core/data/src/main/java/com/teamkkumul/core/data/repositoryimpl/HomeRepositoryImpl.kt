package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toMembersStatus
import com.teamkkumul.core.data.mapper.toPromiseModel
import com.teamkkumul.core.data.mapper.toReadyStatusModel
import com.teamkkumul.core.data.mapper.toTodayMeetingModel
import com.teamkkumul.core.data.mapper.toUserModel
import com.teamkkumul.core.data.repository.HomeRepository
import com.teamkkumul.core.data.utils.handleThrowable
import com.teamkkumul.core.network.api.HomeService
import com.teamkkumul.core.network.dto.request.RequestReadyInfoInputDto
import com.teamkkumul.model.home.HomeMembersStatus
import com.teamkkumul.model.home.HomeReadyStatusModel
import com.teamkkumul.model.home.HomeTodayMeetingModel
import com.teamkkumul.model.home.UserModel
import javax.inject.Inject

internal class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService,
) : HomeRepository {
    override suspend fun getUserInfo(): Result<UserModel> = runCatching {
        homeService.getUserInfo().data?.toUserModel()
    }.mapCatching {
        requireNotNull(it)
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun getTodayMeeting(): Result<HomeTodayMeetingModel?> = runCatching {
        homeService.getTodayMeeting().data?.toTodayMeetingModel()
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun getUpComingMeeting(): Result<List<HomeTodayMeetingModel>> = runCatching {
        homeService.getUpComingMeeting().data?.promises?.map { it.toPromiseModel() }
    }.mapCatching {
        requireNotNull(it)
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun getReadyStatus(promiseId: Int): Result<HomeReadyStatusModel?> =
        runCatching {
            homeService.getReadyStatus(promiseId).data?.toReadyStatusModel()
        }.recoverCatching {
            return it.handleThrowable()
        }

    override suspend fun patchReady(promiseId: Int): Result<Unit> = runCatching {
        homeService.patchReady(promiseId).data
    }.mapCatching {
        requireNotNull(it)
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun patchMoving(promiseId: Int): Result<Unit> = runCatching {
        homeService.patchMoving(promiseId).data
    }.mapCatching {
        requireNotNull(it)
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun patchCompleted(promiseId: Int): Result<Unit> = runCatching {
        homeService.patchCompleted(promiseId).data
    }.mapCatching {
        requireNotNull(it)
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun getMembersReadyStatus(promiseId: Int): Result<List<HomeMembersStatus.Participant?>> =
        runCatching {
            homeService.getMembersReadyStatus(promiseId).data?.participants?.map { it.toMembersStatus() }
        }.mapCatching {
            requireNotNull(it)
        }.recoverCatching {
            return it.handleThrowable()
        }

    override suspend fun patchReadyInfoInput(
        promiseId: Int,
        preparationTime: Int,
        travelTime: Int,
    ): Result<Unit> {
        return runCatching {
            homeService.patchReadyInfoInput(
                promiseId,
                RequestReadyInfoInputDto(preparationTime, travelTime),
            ).data
        }.mapCatching {
            requireNotNull(it)
        }.recoverCatching {
            return it.handleThrowable()
        }
    }
}
