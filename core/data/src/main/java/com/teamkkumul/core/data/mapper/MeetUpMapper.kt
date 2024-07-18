package com.teamkkumul.core.data.mapper

import com.teamkkumul.core.network.dto.response.ResponseMeetUpDetailDto
import com.teamkkumul.core.network.dto.response.ResponseMeetUpParticipantDto
import com.teamkkumul.model.MeetUpDetailModel
import com.teamkkumul.model.MeetUpParticipantModel
import com.teamkkumul.model.MeetUpSealedItem

internal fun ResponseMeetUpParticipantDto.toMeetUpParticipantModel(): MeetUpParticipantModel =
    MeetUpParticipantModel(
        participantCount = participantCount,
        participants = participants.map { participant ->
            MeetUpParticipantModel.Participant(
                memberId = participant.memberId,
                name = participant.name,
                participantId = participant.participantId,
                profileImg = participant.profileImg,
                state = participant.state,
            )
        },
    )

internal fun ResponseMeetUpParticipantDto.toMeetUpSealedItem(): List<MeetUpSealedItem> {
    val items = mutableListOf<MeetUpSealedItem>()

    items.add(MeetUpSealedItem.MyGroupPlus(0))
    participants.forEach {
        items.add(
            MeetUpSealedItem.Participant(
                name = it.name,
                id = it.participantId,
                profileImg = it.profileImg,
            ),
        )
    }
    return items
}

internal fun ResponseMeetUpDetailDto.toMeetUpDetailModel(): MeetUpDetailModel =
    MeetUpDetailModel(
        address = address,
        dressUpLevel = dressUpLevel,
        roadAddress = roadAddress,
        penalty = penalty,
        placeName = placeName,
        time = time,
        promiseName = promiseName,
    )
