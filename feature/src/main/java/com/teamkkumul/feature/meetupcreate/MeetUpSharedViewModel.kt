package com.teamkkumul.feature.meetupcreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamkkumul.feature.utils.MeetUpType
import com.teamkkumul.model.MeetUpCreateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MeetUpSharedViewModel @Inject constructor() : ViewModel() {
    private val _progressLiveData = MutableLiveData<Int>(0)
    val progressLiveData: LiveData<Int> get() = _progressLiveData

    // 초기 MeetUpCreateModel 객체를 StateFlow로 관리
    private val _meetUpCreateModel = MutableStateFlow(
        MeetUpCreateModel(
            name = "",
            placeName = "",
            x = 0.0,
            y = 0.0,
            address = null,
            roadAddress = null,
            time = "",
            participants = emptyList(),
            dressUpLevel = "",
            penalty = "",
            promiseId = -1,
            meetUpType = MeetUpType.CREATE.name,
            date = "",
            meetingId = -1,
        ),
    )

    // 외부에서 접근할 수 있는 StateFlow
    val meetUpCreateModel: StateFlow<MeetUpCreateModel> = _meetUpCreateModel

    fun setProgressBar(value: Int) {
        _progressLiveData.value = value
    }

    // 특정 필드만 업데이트하는 메서드
    fun updateMeetUpModel(
        name: String? = null,
        placeName: String? = null,
        x: Double? = null,
        y: Double? = null,
        address: String? = null,
        roadAddress: String? = null,
        time: String? = null,
        participants: List<Int>? = null,
        dressUpLevel: String? = null,
        penalty: String? = null,
        promiseId: Int? = null,
        meetupType: String? = null,
        date: String? = null,
        meetingId: Int? = null,
    ) {
        // copy 메서드를 사용하여 현재 상태를 기반으로 일부 필드를 업데이트
        _meetUpCreateModel.value = _meetUpCreateModel.value.copy(
            name = name ?: _meetUpCreateModel.value.name,
            placeName = placeName ?: _meetUpCreateModel.value.placeName,
            x = x ?: _meetUpCreateModel.value.x,
            y = y ?: _meetUpCreateModel.value.y,
            address = address ?: _meetUpCreateModel.value.address,
            roadAddress = roadAddress ?: _meetUpCreateModel.value.roadAddress,
            time = time ?: _meetUpCreateModel.value.time,
            participants = participants ?: _meetUpCreateModel.value.participants,
            dressUpLevel = dressUpLevel ?: _meetUpCreateModel.value.dressUpLevel,
            penalty = penalty ?: _meetUpCreateModel.value.penalty,
            promiseId = promiseId ?: _meetUpCreateModel.value.promiseId,
            meetUpType = meetupType ?: _meetUpCreateModel.value.meetUpType,
            date = date ?: _meetUpCreateModel.value.date,
            meetingId = meetingId ?: _meetUpCreateModel.value.meetingId,
        )
    }

    // promiseId, meetingId를 안전하게 반환하는 헬퍼 함수
    fun getPromiseId() = meetUpCreateModel.value.promiseId ?: -1
    fun getMeetingId() = meetUpCreateModel.value.meetingId ?: -1

    fun isEditMode(): Boolean = getMeetUpType() == MeetUpType.EDIT.name

    private fun getMeetUpType(): String =
        meetUpCreateModel.value.meetUpType ?: MeetUpType.CREATE.name
}
