package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toMyGroupInfoModel
import com.teamkkumul.core.data.mapper.toMyGroupMeetUpModel
import com.teamkkumul.core.data.mapper.toMyGroupMemberModel
import com.teamkkumul.core.data.mapper.toMyGroupMemberToMeetUp
import com.teamkkumul.core.data.mapper.toMyGroupModel
import com.teamkkumul.core.data.mapper.toMyGroupSealedItem
import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.core.data.utils.handleThrowable
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
        myGroupService.getMyGroupList().data?.toMyGroupModel()
    }.mapCatching { myGroupModel ->
        requireNotNull(myGroupModel)
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun getMyGroupMeetUp(
        meetingId: Int,
        done: Boolean,
        isParticipant: Boolean?,
    ): Result<List<MyGroupMeetUpModel.Promise>> =
        runCatching {
            myGroupService.getMyGroupMeetUp(
                meetingId,
                done,
                isParticipant,
            ).data?.toMyGroupMeetUpModel()
        }.mapCatching {
            requireNotNull(it)
        }.recoverCatching {
            return it.handleThrowable()
        }

    override suspend fun getMyGroupInfo(meetingId: Int): Result<MyGroupInfoModel> = runCatching {
        myGroupService.getMyGroupInfo(meetingId).data?.toMyGroupInfoModel()
    }.mapCatching {
        requireNotNull(it)
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun getMyGroupMemberToMeetUp(meetingId: Int): Result<List<MyGroupMemberModel.Member>> =
        runCatching {
            val response = myGroupService.getMyGroupMember(meetingId)
            response.data?.toMyGroupMemberToMeetUp() ?: throw Exception("null")
        }

    override suspend fun getMyGroupMemberList(meetingId: Int): Result<List<MyGroupDetailMemeberSealedItem>> =
        runCatching {
            myGroupService.getMyGroupMemberPlus(meetingId).data?.toMyGroupSealedItem()
        }.mapCatching {
            requireNotNull(it)
        }.recoverCatching {
            return it.handleThrowable()
        }

    override suspend fun getMyGroupMember(meetingId: Int): Result<MyGroupMemberModel> =
        runCatching {
            myGroupService.getMyGroupMemberPlus(meetingId).data?.toMyGroupMemberModel()
        }.mapCatching {
            requireNotNull(it)
        }.recoverCatching {
            return it.handleThrowable()
        }

    override suspend fun deleteMyGroup(meetingId: Int): Result<Unit> = runCatching {
        myGroupService.deleteMyGroup(meetingId)
        Unit
    }.recoverCatching {
        return it.handleThrowable()
    }
}
