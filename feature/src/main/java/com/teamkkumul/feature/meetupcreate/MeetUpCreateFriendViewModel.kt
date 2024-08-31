package com.teamkkumul.feature.meetupcreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MeetUpCreateLocationRepository
import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.MeetUpEditParticipantModel
import com.teamkkumul.model.MyGroupMemberModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetUpCreateFriendViewModel @Inject constructor(
    private val meetUpCreateLocationRepository: MeetUpCreateLocationRepository,
    private val myGroupRepository: MyGroupRepository,
) : ViewModel() {

    private val _meetUpCreateMemberState =
        MutableStateFlow<UiState<List<MyGroupMemberModel.Member>>>(UiState.Loading)
    val meetUpCreateMemberState get() = _meetUpCreateMemberState.asStateFlow()

    private val _meetUpEditMemberState =
        MutableStateFlow<UiState<List<MeetUpEditParticipantModel.Member>?>>(UiState.Loading)
    val meetUpEditMemberState get() = _meetUpEditMemberState.asStateFlow()

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
}
