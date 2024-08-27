package com.teamkkumul.feature.meetupcreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MeetUpCreateLocationRepository
import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.core.ui.view.UiState
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
}
