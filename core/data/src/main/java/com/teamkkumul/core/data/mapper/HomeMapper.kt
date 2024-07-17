package com.teamkkumul.core.data.mapper

import com.teamkkumul.core.network.dto.response.ResponseTodayMeetingDto
import com.teamkkumul.core.network.dto.response.ResponseUserDto
import com.teamkkumul.model.HomeTodayMeetingModel
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
