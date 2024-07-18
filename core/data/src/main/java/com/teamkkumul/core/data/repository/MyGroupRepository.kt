package com.teamkkumul.core.data.repository

import com.teamkkumul.model.MyGroupDetailMemeberSealedItem
import com.teamkkumul.model.MyGroupInfoModel
import com.teamkkumul.model.MyGroupMeetUpModel
import com.teamkkumul.model.MyGroupMemberModel
import com.teamkkumul.model.MyGroupModel

interface MyGroupRepository {
    suspend fun getMyGroup(): Result<MyGroupModel>
    suspend fun getMyGroupMeetUp(
        meetingId: Int,
        done: Boolean,
    ): Result<List<MyGroupMeetUpModel.Promise>>

    suspend fun getMyGroupMember(meetingId: Int): Result<MyGroupMemberModel>
    suspend fun getMyGroupMemberList(meetingId: Int): Result<List<MyGroupDetailMemeberSealedItem>>
    suspend fun getMyGroupInfo(meetingId: Int): Result<MyGroupInfoModel>
    suspend fun getMyGroupMemberToMeetUp(meetingId: Int): Result<List<MyGroupMemberModel.Member>>
}
