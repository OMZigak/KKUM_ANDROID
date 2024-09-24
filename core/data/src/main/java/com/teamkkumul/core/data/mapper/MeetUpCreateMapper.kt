package com.teamkkumul.core.data.mapper

import com.teamkkumul.core.network.dto.request.RequestMeetUpCreateDto
import com.teamkkumul.core.network.dto.response.ResponseMeetUpCreateLocationDto
import com.teamkkumul.core.network.dto.response.ResponseMeetUpEditParticipantDto
import com.teamkkumul.model.MeetUpCreateLocationModel
import com.teamkkumul.model.MeetUpCreateModel
import com.teamkkumul.model.MeetUpEditParticipantModel

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

internal fun ResponseMeetUpEditParticipantDto.toMeetUpEditParticipant(): List<MeetUpEditParticipantModel.Member> =
    members.map { member ->
        MeetUpEditParticipantModel.Member(
            memberId = member.memberId,
            profileImg = member.profileImg,
            name = member.name,
            isParticipant = member.isParticipant,
        )
    }
