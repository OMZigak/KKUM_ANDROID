package com.teamkkumul.core.data.mapper

import com.teamkkumul.core.network.dto.response.ResponseHomeMembersReadyStatus
import com.teamkkumul.core.network.dto.response.ResponseHomeReadyStatusDto
import com.teamkkumul.core.network.dto.response.ResponseHomeUpComingMeetingDto
import com.teamkkumul.core.network.dto.response.ResponseTodayMeetingDto
import com.teamkkumul.core.network.dto.response.ResponseUserDto
import com.teamkkumul.model.home.HomeMembersStatus
import com.teamkkumul.model.home.HomeReadyStatusModel
import com.teamkkumul.model.home.HomeTodayMeetingModel
import com.teamkkumul.model.home.UserModel

internal fun ResponseUserDto.toUserModel(): UserModel =
    UserModel(
        userId,
        level,
        name,
        profileImg,
        promiseCount,
        tardyCount,
        tardySum,
    )

internal fun ResponseTodayMeetingDto.toTodayMeetingModel(): HomeTodayMeetingModel =
    HomeTodayMeetingModel(
        dDay = dDay,
        date = date,
        name = name,
        promiseId = promiseId,
        meetingName = meetingName,
        dressUpLevel = dressUpLevel,
        time = time,
        placeName = placeName,
    )

internal fun ResponseHomeUpComingMeetingDto.Promise.toPromiseModel(): HomeTodayMeetingModel =
    HomeTodayMeetingModel(
        dDay = dDay,
        date = date,
        name = name,
        promiseId = promiseId,
        meetingName = meetingName,
        dressUpLevel = dressUpLevel,
        time = time,
        placeName = placeName,
    )

internal fun ResponseHomeReadyStatusDto.toReadyStatusModel(): HomeReadyStatusModel =
    HomeReadyStatusModel(
        arrivalAt,
        departureAt,
        preparationStartAt,
        preparationTime,
        promiseTime,
        travelTime,
    )

internal fun ResponseHomeMembersReadyStatus.Participant.toMembersStatus(): HomeMembersStatus.Participant =
    HomeMembersStatus.Participant(
        memberId,
        name,
        participantId,
        profileImg,
        state,
    )
