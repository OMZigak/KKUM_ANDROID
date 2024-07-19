package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toMyGroupInfoModel
import com.teamkkumul.core.data.mapper.toMyGroupMeetUpModel
import com.teamkkumul.core.data.mapper.toMyGroupMemberModel
import com.teamkkumul.core.data.mapper.toMyGroupMemberToMeetUp
import com.teamkkumul.core.data.mapper.toMyGroupModel
import com.teamkkumul.core.data.mapper.toMyGroupSealedItem
import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.core.network.api.MyGroupService
import com.teamkkumul.model.MyGroupDetailMemeberSealedItem
import com.teamkkumul.model.MyGroupInfoModel
import com.teamkkumul.model.MyGroupMeetUpModel
import com.teamkkumul.model.MyGroupMemberModel
import com.teamkkumul.model.MyGroupModel
import javax.inject.Inject

class MyGroupRepositoryImpl @Inject constructor(
    private val myGroupService: MyGroupService,
) : MyGroupRepository {
    override suspend fun getMyGroup(): Result<MyGroupModel> = runCatching {
        val response = myGroupService.getMyGroupList()
        response.data?.toMyGroupModel() ?: throw Exception("null")
    }

    override suspend fun getMyGroupMeetUp(
        meetingId: Int,
        done: Boolean,
    ): Result<List<MyGroupMeetUpModel.Promise>> =
        runCatching {
            val response = myGroupService.getMyGroupMeetUp(meetingId, done)
            response.data?.toMyGroupMeetUpModel() ?: throw Exception("null")
        }

    override suspend fun getMyGroupInfo(meetingId: Int): Result<MyGroupInfoModel> = runCatching {
        val response = myGroupService.getMyGroupInfo(meetingId)
        response.data?.toMyGroupInfoModel() ?: throw Exception("null")
    }

    override suspend fun getMyGroupMemberToMeetUp(meetingId: Int): Result<List<MyGroupMemberModel.Member>> =
        runCatching {
            val response = myGroupService.getMyGroupMember(meetingId)
            response.data?.toMyGroupMemberToMeetUp() ?: throw Exception("null")
        }

    override suspend fun getMyGroupMemberList(meetingId: Int): Result<List<MyGroupDetailMemeberSealedItem>> {
        return runCatching {
            val response = myGroupService.getMyGroupMemberPlus(meetingId)
            response.data?.toMyGroupSealedItem() ?: throw Exception("null")
        }
    }

    override suspend fun getMyGroupMember(meetingId: Int): Result<MyGroupMemberModel> =
        runCatching {
            val response = myGroupService.getMyGroupMemberPlus(meetingId)
            response.data?.toMyGroupMemberModel() ?: throw Exception("null")
        }
}
