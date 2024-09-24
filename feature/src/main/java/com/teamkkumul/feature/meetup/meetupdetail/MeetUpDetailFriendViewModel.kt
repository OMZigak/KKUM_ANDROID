package com.teamkkumul.feature.meetup.meetupdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MeetUpRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.MeetUpDetailModel
import com.teamkkumul.model.MeetUpParticipantItem
import com.teamkkumul.model.MeetUpParticipantModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetUpDetailFriendViewModel @Inject constructor(
    private val meetUpRepository: MeetUpRepository,
) : ViewModel() {
    private val _meetUpDetailState = MutableStateFlow<UiState<MeetUpDetailModel>>(UiState.Loading)
    val meetupDetailState get() = _meetUpDetailState.asStateFlow()

    private val _meetUpParticipantState =
        MutableStateFlow<UiState<MeetUpParticipantModel>>(UiState.Loading)
    val meetUpParticipantState get() = _meetUpParticipantState.asStateFlow()

    private val _meetUpParticipantListState =
        MutableStateFlow<UiState<List<MeetUpParticipantItem>>>(UiState.Loading)
    val meetUpParticipantListState get() = _meetUpParticipantListState.asStateFlow()

    fun getMeetUpDetail(promiseId: Int) = viewModelScope.launch {
        meetUpRepository.getMeetUpDetail(promiseId)
            .onSuccess { meetUpModel ->
                _meetUpDetailState.emit(UiState.Success(meetUpModel))
            }
            .onFailure { exception ->
                _meetUpDetailState.emit(UiState.Failure(exception.message.toString()))
            }
    }

    fun getMeetUpParticipant(promiseId: Int) = viewModelScope.launch {
        meetUpRepository.getMeetUpParticipant(promiseId)
            .onSuccess { meetUpParticipantModel ->
                _meetUpParticipantState.emit(UiState.Success(meetUpParticipantModel))
            }.onFailure { exception ->
                _meetUpParticipantState.emit(UiState.Failure(exception.message.toString()))
            }
    }

    fun getMeetUpParticipantList(promiseId: Int) {
        viewModelScope.launch {
            meetUpRepository.getMeetUpFriendList(promiseId)
                .onSuccess {
                    _meetUpParticipantListState.emit(UiState.Success(it))
                }.onFailure { exception ->
                    _meetUpParticipantListState.emit(UiState.Failure(exception.message.toString()))
                }
        }
    }
}
