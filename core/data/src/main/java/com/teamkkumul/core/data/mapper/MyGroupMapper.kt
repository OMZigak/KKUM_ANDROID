package com.teamkkumul.core.data.mapper

import com.teamkkumul.core.network.dto.response.ResponseMyGroupDto
import com.teamkkumul.core.network.dto.response.ResponseMyGroupInfoDto
import com.teamkkumul.core.network.dto.response.ResponseMyGroupMeetUpDto
import com.teamkkumul.core.network.dto.response.ResponseMyGroupMemberDto
import com.teamkkumul.model.MyGroupDetailMemeberSealedItem
import com.teamkkumul.model.MyGroupInfoModel
import com.teamkkumul.model.MyGroupMeetUpModel
import com.teamkkumul.model.MyGroupMemberModel
import com.teamkkumul.model.MyGroupModel

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

internal fun ResponseMyGroupMeetUpDto.toMyGroupMeetUpModel(): List<MyGroupMeetUpModel.Promise> =
    promises.map { promise ->
        MyGroupMeetUpModel.Promise(
            dDay = promise.dDay,
            date = promise.date,
            name = promise.name,
            placeName = promise.placeName,
            time = promise.time,
            promiseId = promise.promiseId,
        )
    }

internal fun ResponseMyGroupInfoDto.toMyGroupInfoModel(): MyGroupInfoModel =
    MyGroupInfoModel(
        meetingId = meetingId,
        name = name,
        createdAt = createdAt,
        metCount = metCount,
        invitationCode = invitationCode,
    )

internal fun ResponseMyGroupMemberDto.toMyGroupSealedItem(): List<MyGroupDetailMemeberSealedItem> {
    val items = mutableListOf<MyGroupDetailMemeberSealedItem>()

    items.add(MyGroupDetailMemeberSealedItem.MyGroupDetailMemeberPlus(1))
    members.forEach {
        items.add(
            MyGroupDetailMemeberSealedItem.Member(
                name = it.name,
                memberId = it.memberId,
                profileImg = it.profileImg,
            ),
        )
    }
    return items
}

internal fun ResponseMyGroupMemberDto.toMyGroupMemberToMeetUp(): List<MyGroupMemberModel.Member> =
    members.map { member ->
        MyGroupMemberModel.Member(
            memberId = member.memberId,
            profileImg = member.profileImg,
            name = member.name,
        )
    }

internal fun ResponseMyGroupMemberDto.toMyGroupMemberModel(): MyGroupMemberModel =
    MyGroupMemberModel(
        memberCount = memberCount,
        members = members.map { member ->
            MyGroupMemberModel.Member(
                memberId = member.memberId,
                name = member.name,
                profileImg = member.profileImg,
            )
        },
    )
