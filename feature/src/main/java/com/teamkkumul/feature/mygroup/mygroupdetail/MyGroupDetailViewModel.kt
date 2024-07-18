package com.teamkkumul.feature.mygroup.mygroupdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.MyGroupDetailMemeberSealedItem
import com.teamkkumul.model.MyGroupInfoModel
import com.teamkkumul.model.MyGroupMeetUpModel
import com.teamkkumul.model.MyGroupMemberModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGroupDetailViewModel @Inject constructor(
    private val myGroupRepository: MyGroupRepository,
) : ViewModel() {

    private val _myGroupInfoState = MutableStateFlow<UiState<MyGroupInfoModel>>(UiState.Loading)
    val myGroupInfoState get() = _myGroupInfoState.asStateFlow()

    private val _myGroupMemberState =
        MutableStateFlow<UiState<MyGroupMemberModel>>(UiState.Loading)
    val myGroupMemberState get() = _myGroupMemberState.asStateFlow()

    private val _myGroupMemberListState =
        MutableStateFlow<UiState<List<MyGroupDetailMemeberSealedItem>>>(UiState.Loading)
    val myGroupMemberListState get() = _myGroupMemberListState.asStateFlow()

    private val _myGroupMeetUpState =
        MutableStateFlow<UiState<List<MyGroupMeetUpModel.Promise>>>(UiState.Loading)
    val myGroupMeetUpState get() = _myGroupMeetUpState.asStateFlow()

    fun getMyGroupInfo(meetingId: Int) = viewModelScope.launch {
        myGroupRepository.getMyGroupInfo(meetingId)
            .onSuccess { myGroupInfoModel ->
                _myGroupInfoState.emit(UiState.Success(myGroupInfoModel))
            }
            .onFailure { exception ->
                _myGroupInfoState.emit(UiState.Failure(exception.message.toString()))
            }
    }

    fun getMyGroupMember(meetingId: Int) = viewModelScope.launch {
        myGroupRepository.getMyGroupMember(meetingId)
            .onSuccess { myGroupMemberModel ->
                _myGroupMemberState.emit(UiState.Success(myGroupMemberModel))
            }.onFailure { exception ->
                _myGroupMemberState.emit(UiState.Failure(exception.message.toString()))
            }
    }

    fun getMyGroupMemberList(meetingId: Int) = viewModelScope.launch {
        myGroupRepository.getMyGroupMemberList(meetingId)
            .onSuccess {
                _myGroupMemberListState.emit(UiState.Success(it))
            }.onFailure { exception ->
                _myGroupMemberListState.emit(UiState.Failure(exception.message.toString()))
            }
    }

    fun getMyGroupMeetUp(meetingId: Int, done: Boolean) = viewModelScope.launch {
        myGroupRepository.getMyGroupMeetUp(meetingId, done)
            .onSuccess { myGroupMemberModel ->
                if (myGroupMemberModel.isEmpty()) {
                    _myGroupMeetUpState.emit(UiState.Empty)
                } else {
                    _myGroupMeetUpState.emit(UiState.Success(myGroupMemberModel))
                }
                _myGroupMeetUpState.emit(UiState.Success(myGroupMemberModel))
            }.onFailure { exception ->
                _myGroupMeetUpState.emit(UiState.Failure(exception.message.toString()))
            }
    }
}
