package com.teamkkumul.core.data.mapper

import com.teamkkumul.core.network.dto.response.ResponseMyGroupFriendDto
import com.teamkkumul.model.MyGroupSealedItem

internal fun ResponseMyGroupFriendDto.toMyGroupModel(): List<MyGroupSealedItem.Member> =
    members.map {
        MyGroupSealedItem.Member(
            id = it.id,
            name = it.name,
            profileImg = it.profileImg,
        )
    }
