package com.teamkkumul.feature.meetup.readystatus.readystatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.utils.model.BtnState
import com.teamkkumul.model.MeetUpDetailFriendModel
import com.teamkkumul.model.MyGroupMeetUpModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReadyStatusViewModel : ViewModel() {
    private val _readyBtnState =
        MutableStateFlow<BtnState>(BtnState.Default(isEnabled = true))
    val readyBtnState: StateFlow<BtnState> get() = _readyBtnState

    private val _movingStartBtnState =
        MutableStateFlow<BtnState>(BtnState.DefaultGray(isEnabled = false))
    val movingStartBtnState: StateFlow<BtnState> get() = _movingStartBtnState

    private val _completedBtnState =
        MutableStateFlow<BtnState>(BtnState.DefaultGray(isEnabled = false))
    val completedBtnState: StateFlow<BtnState> get() = _completedBtnState

    private val _homePromiseState =
        MutableStateFlow<UiState<List<MyGroupMeetUpModel.Promise>>>(UiState.Loading)
    val homePromiseState get() = _homePromiseState.asStateFlow()

    fun clickReadyBtn() {
        viewModelScope.launch {
            if (isCompleteState(_readyBtnState)) return@launch
            _readyBtnState.emit(BtnState.InProgress(isEnabled = false))
            _movingStartBtnState.emit(BtnState.Default(isEnabled = true))
            _completedBtnState.emit(BtnState.DefaultGray(isEnabled = false))
        }
    }

    fun clickMovingStartBtn() {
        viewModelScope.launch {
            if (isCompleteState(_movingStartBtnState)) return@launch
            _readyBtnState.emit(BtnState.Complete(isEnabled = false))
            _movingStartBtnState.emit(BtnState.InProgress(isEnabled = false))
            _completedBtnState.emit(BtnState.Default(isEnabled = true))
        }
    }

    fun clickCompletedBtn() {
        viewModelScope.launch {
            if (isCompleteState(_completedBtnState)) return@launch
            _readyBtnState.emit(BtnState.Complete(isEnabled = false))
            _movingStartBtnState.emit(BtnState.Complete(isEnabled = false))
            _completedBtnState.emit(BtnState.Complete(isEnabled = false))
        }
    }

    private fun isCompleteState(stateFlow: StateFlow<BtnState>): Boolean {
        return stateFlow.value is BtnState.Complete
    }

    val mockMembers = listOf(
        MeetUpDetailFriendModel.Participant(
            id = 2,
            name = "Eric",
            profileImg = "https://example.com/alice.jpg",
            state = "이동중",
        ),
        MeetUpDetailFriendModel.Participant(
            id = 3,
            name = "Bob",
            profileImg = "https://example.com/alice.jpg",
            state = "이동중",
        ),
        MeetUpDetailFriendModel.Participant(
            id = 2,
            name = "Alice",
            profileImg = "https://example.com/alice.jpg",
            state = "이동중",
        ),
        MeetUpDetailFriendModel.Participant(
            id = 2,
            name = "Eric",
            profileImg = "https://example.com/alice.jpg",
            state = "이동중",
        ),
        MeetUpDetailFriendModel.Participant(
            id = 3,
            name = "Bob",
            profileImg = "https://example.com/alice.jpg",
            state = "이동중",
        ),
        MeetUpDetailFriendModel.Participant(
            id = 2,
            name = "Alice",
            profileImg = "https://example.com/alice.jpg",
            state = "이동중",
        ),
    )
}
