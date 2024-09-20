package com.chanu.core.domain.usecase

import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.model.MyGroupMeetUpModel
import javax.inject.Inject

class GetMyGroupMeetUpUseCase @Inject constructor(
    private val myGroupRepository: MyGroupRepository,
) {
    suspend operator fun invoke(
        meetingId: Int,
        done: Boolean,
        isParticipant: Boolean? = null,
    ): Result<List<MyGroupMeetUpModel.Promise>> =
        myGroupRepository.getMyGroupMeetUp(meetingId, done, isParticipant)
            .mapCatching { promises ->
                promises.sortedWith(
                    compareBy<MyGroupMeetUpModel.Promise> {
                        when {
                            it.dDay == 0 -> -1
                            it.dDay < 0 -> 0
                            else -> 1
                        }
                    }.thenByDescending { it.dDay.takeIf { d -> d < 0 } } // 음수는 내림차순 정렬
                        .thenBy { it.dDay.takeIf { d -> d > 0 } }, // 양수는 오름차순 정렬
                )
            }
}
