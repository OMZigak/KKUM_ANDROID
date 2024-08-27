package com.teamkkumul.feature.meetupcreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MeetUpCreateLocationRepository
import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.utils.MeetUpType
import com.teamkkumul.model.MeetUpCreateLocationModel
import com.teamkkumul.model.MeetUpCreateModel
import com.teamkkumul.model.MeetUpEditParticipantModel
import com.teamkkumul.model.MyGroupMemberModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetUpCreateViewModel @Inject constructor(
    private val meetUpCreateLocationRepository: MeetUpCreateLocationRepository,
    private val myGroupRepository: MyGroupRepository,
) : ViewModel() {
    private val _meetUpInputState = MutableStateFlow(false)
    val meetUpInputState: StateFlow<Boolean> get() = _meetUpInputState

    private val _meetUpLocation = MutableStateFlow<String>("")
    val meetUpLocation: StateFlow<String> get() = _meetUpLocation

    private val _meetUpLocationX = MutableStateFlow<String>("")
    val meetUpLocationX: StateFlow<String> get() = _meetUpLocationX

    private val _meetUpLocationY = MutableStateFlow<String>("")
    val meetUpLocationY: StateFlow<String> get() = _meetUpLocationY

    private val _meetUpDate = MutableStateFlow<String>("")
    val meetUpDate: StateFlow<String> get() = _meetUpDate

    private val _meetUpTime = MutableStateFlow<String>("")
    val meetUpTime: StateFlow<String> get() = _meetUpTime

    private val _meetUpCreateLocationState =
        MutableStateFlow<UiState<List<MeetUpCreateLocationModel.Location>>>(UiState.Loading)
    val meetUpCreateLocationState get() = _meetUpCreateLocationState.asStateFlow()

    private val _progressLiveData = MutableLiveData<Int>(0)
    val progressLiveData: LiveData<Int> get() = _progressLiveData

    private val _meetUpCreateMemberState =
        MutableStateFlow<UiState<List<MyGroupMemberModel.Member>>>(UiState.Loading)
    val meetUpCreateMemberState get() = _meetUpCreateMemberState.asStateFlow()

    private val _meetUpName = MutableStateFlow<Boolean>(false)

    private val _meetUpCreateState = MutableSharedFlow<UiState<Int>>()
    val meetUpCreateState get() = _meetUpCreateState.asSharedFlow()

    private val _meetUpEditMemberState =
        MutableStateFlow<UiState<List<MeetUpEditParticipantModel.Member>?>>(UiState.Loading)
    val meetUpEditMemberState get() = _meetUpEditMemberState.asStateFlow()

    private val _meetUpEditState = MutableSharedFlow<UiState<Int>>()
    val meetUpEditState get() = _meetUpEditState.asSharedFlow()

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
        ),
    )

    // 외부에서 접근할 수 있는 StateFlow
    val meetUpCreateModel: StateFlow<MeetUpCreateModel> = _meetUpCreateModel

    fun postMeetUpCreate(meetingId: Int, meetUpCreateModel: MeetUpCreateModel) {
        viewModelScope.launch {
            meetUpCreateLocationRepository.postMeetUpCreate(meetingId, meetUpCreateModel)
                .onSuccess {
                    _meetUpCreateState.emit(UiState.Success(it))
                }.onFailure {
                    _meetUpCreateState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }

    fun putMeetUpEdit(promiseId: Int, meetUpCreateModel: MeetUpCreateModel) {
        viewModelScope.launch {
            meetUpCreateLocationRepository.putMeetUpEdit(promiseId, meetUpCreateModel)
                .onSuccess {
                    _meetUpEditState.emit(UiState.Success(it))
                }.onFailure {
                    _meetUpEditState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }

    fun setMeetUpDate(input: String) {
        viewModelScope.launch {
            _meetUpDate.emit(input)
            validateForm()
        }
    }

    fun setMeetUpTime(input: String) {
        viewModelScope.launch {
            _meetUpTime.emit(input)
            validateForm()
        }
    }

    fun setProgressBar(value: Int) {
        _progressLiveData.value = value
    }

    fun setMeetUpLocation(location: String) {
        viewModelScope.launch {
            _meetUpLocation.emit(location)
            validateForm()
        }
    }

    fun setMeetUpLocationX(x: String) {
        viewModelScope.launch {
            _meetUpLocationX.emit(x)
        }
    }

    fun setMeetUpLocationY(y: String) {
        viewModelScope.launch {
            _meetUpLocationY.emit(y)
        }
    }

    fun setMeetUpName(input: Boolean) {
        viewModelScope.launch {
            _meetUpName.emit(input)
            validateForm()
        }
    }

    fun getMeetUpCreateLocation(q: String) = viewModelScope.launch {
        meetUpCreateLocationRepository.getMeetUpCreateLocation(q)
            .onSuccess { meetUpCreateLocationRepository ->
                _meetUpCreateLocationState.emit(UiState.Success(meetUpCreateLocationRepository))
            }.onFailure { exception ->
                _meetUpCreateLocationState.emit(UiState.Failure(exception.message.toString()))
            }
    }

    fun getMyGroupMemberToMeetUp(memberId: Int) = viewModelScope.launch {
        myGroupRepository.getMyGroupMemberToMeetUp(memberId)
            .onSuccess { meetUpCreateMemberModel ->
                if (meetUpCreateMemberModel.isEmpty()) {
                    _meetUpCreateMemberState.emit(UiState.Empty)
                } else {
                    _meetUpCreateMemberState.emit(UiState.Success(meetUpCreateMemberModel))
                }
            }.onFailure { exception ->
                _meetUpCreateMemberState.emit(UiState.Failure(exception.message.toString()))
            }
    }

    fun getMeetUpEditMember(promiseId: Int) = viewModelScope.launch {
        meetUpCreateLocationRepository.getMeetUpEditParticipant(promiseId)
            .onSuccess { meetUpEditParticipantModel ->
                if (meetUpEditParticipantModel.isEmpty()) {
                    _meetUpEditMemberState.emit(UiState.Empty)
                } else {
                    _meetUpEditMemberState.emit(UiState.Success(meetUpEditParticipantModel))
                }
            }.onFailure { exception ->
                _meetUpEditMemberState.emit(UiState.Failure(exception.message.toString()))
            }
    }

    fun setEmptyLocationList() {
        viewModelScope.launch {
            _meetUpCreateLocationState.emit(UiState.Loading)
        }
    }

    private fun validateForm() {
        viewModelScope.launch {
            val isFormValid =
                _meetUpLocation.value.isNotEmpty() &&
                    _meetUpDate.value.isNotEmpty() &&
                    _meetUpTime.value.isNotEmpty() &&
                    _meetUpName.value

            _meetUpInputState.emit(isFormValid)
        }
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
        )
    }

    // promiseId를 안전하게 반환하는 헬퍼 함수
    fun getPromiseId() = meetUpCreateModel.value.promiseId ?: -1

    fun isEditMode(): Boolean = getMeetUpType() == MeetUpType.EDIT.name

    private fun getMeetUpType(): String =
        meetUpCreateModel.value.meetUpType ?: MeetUpType.CREATE.name
}
