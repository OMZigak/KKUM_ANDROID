package com.teamkkumul.core.data.mapper

import com.teamkkumul.core.network.dto.response.ResponseMyGroupDto
import com.teamkkumul.core.network.dto.response.ResponseMyGroupFriendDto
import com.teamkkumul.model.MyGroupDetailSealedItem
import com.teamkkumul.model.MyGroupModel

internal fun ResponseMyGroupFriendDto.toMyGroupModel(): List<MyGroupDetailSealedItem.Member> =
    members.map {
        MyGroupDetailSealedItem.Member(
            id = it.id,
            name = it.name,
            profileImg = it.profileImg,
        )
    }

internal fun ResponseMyGroupDto.toMyGroupModel(): MyGroupModel =
    MyGroupModel(
        count = count,
        meetings = meetings.map { meeting ->
            MyGroupModel.Meeting(
                id = meeting.meetingId,
                memberCount = meeting.memberCount,
                name = meeting.name,
            )
        },
    )
