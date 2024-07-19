package com.teamkkumul.core.data.mapper

import com.teamkkumul.core.network.dto.request.RequestMeetUpCreateDto
import com.teamkkumul.core.network.dto.response.ResponseMeetUpCreateLocationDto
import com.teamkkumul.model.MeetUpCreateLocationModel
import com.teamkkumul.model.MeetUpCreateModel

internal fun ResponseMeetUpCreateLocationDto.toMeetUpCreateLocationModel(): List<MeetUpCreateLocationModel.Location> =
    locations.map { location ->
        MeetUpCreateLocationModel.Location(
            address = location.address,
            roadAddress = location.roadAddress,
            location = location.location,
            x = location.x,
            y = location.y,
        )
    }

fun MeetUpCreateModel.toRequestMeetUpCreateDto(): RequestMeetUpCreateDto {
    return RequestMeetUpCreateDto(
        name = name,
        placeName = placeName,
        x = x,
        y = y,
        address = address,
        roadAddress = roadAddress,
        time = time,
        participants = participants,
        dressUpLevel = dressUpLevel,
        penalty = penalty,
    )
}
