package com.teamkkumul.core.data.mapper

import com.teamkkumul.core.network.dto.response.ResponseMeetUpCreateLocationDto
import com.teamkkumul.model.MeetUpCreateLocationModel

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
