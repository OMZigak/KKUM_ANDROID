package com.teamkkumul.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.HomeRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.utils.model.BtnState
import com.teamkkumul.model.home.HomeReadyStatusModel
import com.teamkkumul.model.home.HomeTodayMeetingModel
import com.teamkkumul.model.home.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _readyBtnState =
        MutableStateFlow<BtnState>(BtnState.Default(isEnabled = true))
    val readyBtnState: StateFlow<BtnState> get() = _readyBtnState

    private val _movingStartBtnState =
        MutableStateFlow<BtnState>(BtnState.DefaultGray(isEnabled = false))
    val movingStartBtnState: StateFlow<BtnState> get() = _movingStartBtnState

    private val _completedBtnState =
        MutableStateFlow<BtnState>(BtnState.DefaultGray(isEnabled = false))
    val completedBtnState: StateFlow<BtnState> get() = _completedBtnState

    private val _homeState = MutableStateFlow<UiState<UserModel>>(UiState.Loading)
    val homeState: StateFlow<UiState<UserModel>> = _homeState.asStateFlow()

    private val _todayMeetingState = MutableStateFlow<UiState<HomeTodayMeetingModel?>>(
        UiState.Loading,
    )
    val todayMeetingState: StateFlow<UiState<HomeTodayMeetingModel?>> =
        _todayMeetingState.asStateFlow()

    private val _upComingMeetingState =
        MutableStateFlow<UiState<List<HomeTodayMeetingModel>>>(UiState.Loading)
    val upComingMeetingState: StateFlow<UiState<List<HomeTodayMeetingModel>>> =
        _upComingMeetingState.asStateFlow()

    private val _readyStatusState =
        MutableStateFlow<UiState<HomeReadyStatusModel?>>(UiState.Loading)
    val readyStatusState: StateFlow<UiState<HomeReadyStatusModel?>> =
        _readyStatusState.asStateFlow()

    private val _isReady = MutableSharedFlow<Boolean>()
    val isReady: SharedFlow<Boolean> get() = _isReady.asSharedFlow()

    private val _isMoving = MutableStateFlow(false)
    val isMoving: StateFlow<Boolean> get() = _isMoving

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> get() = _isCompleted

    fun getReadyStatus(promiseId: Int) {
        viewModelScope.launch {
            homeRepository.getReadyStatus(promiseId)
                .onSuccess {
                    if (it == null) {
                        _readyStatusState.emit(UiState.Empty)
                    } else {
                        _readyStatusState.emit(UiState.Success(it))
                        _isReady.emit(true)
                    }
                }.onFailure {
                    _readyStatusState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }

    fun getUpComingMeeting() {
        viewModelScope.launch {
            homeRepository.getUpComingMeeting()
                .onSuccess {
                    if (it.isEmpty()) {
                        _upComingMeetingState.emit(UiState.Empty)
                    } else {
                        _upComingMeetingState.emit(UiState.Success(it))
                    }
                }.onFailure {
                    _upComingMeetingState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }

    fun getTodayMeeting() {
        viewModelScope.launch {
            homeRepository.getTodayMeeting()
                .onSuccess {
                    if (it == null) {
                        _todayMeetingState.emit(UiState.Empty)
                    } else {
                        _todayMeetingState.emit(UiState.Success(it))
                    }
                }.onFailure {
                    _todayMeetingState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }

    fun getUserInfo() {
        viewModelScope.launch {
            homeRepository.getUserInfo()
                .onSuccess {
                    _homeState.emit(UiState.Success(it))
                }.onFailure {
                    _homeState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }

    fun patchReady(promiseId: Int) {
        viewModelScope.launch {
            homeRepository.patchReady(promiseId = promiseId).onSuccess {
                clickReadyBtn()
            }
        }
    }

    fun patchMoving(promiseId: Int) {
        viewModelScope.launch {
            homeRepository.patchMoving(promiseId = promiseId).onSuccess {
                clickMovingStartBtn()
            }
        }
    }

    fun patchCompleted(promiseId: Int) {
        viewModelScope.launch {
            homeRepository.patchCompleted(promiseId = promiseId).onSuccess {
                clickCompletedBtn()
            }
        }
    }

    fun clickReadyBtn() {
        viewModelScope.launch {
            if (isCompleteState(_readyBtnState)) return@launch
            _readyBtnState.emit(BtnState.InProgress(isEnabled = false))
            _movingStartBtnState.emit(BtnState.Default(isEnabled = true))
            _completedBtnState.emit(BtnState.DefaultGray(isEnabled = false))
            _isReady.emit(false)
            _isMoving.emit(true)
        }
    }

    fun clickMovingStartBtn() {
        viewModelScope.launch {
            if (isCompleteState(_movingStartBtnState)) return@launch
            _readyBtnState.emit(BtnState.Complete(isEnabled = false))
            _movingStartBtnState.emit(BtnState.InProgress(isEnabled = false))
            _completedBtnState.emit(BtnState.Default(isEnabled = true))
            _isMoving.emit(false)
            _isCompleted.emit(true)
        }
    }

    fun clickCompletedBtn() {
        viewModelScope.launch {
            if (isCompleteState(_completedBtnState)) return@launch
            _readyBtnState.emit(BtnState.Complete(isEnabled = false))
            _movingStartBtnState.emit(BtnState.Complete(isEnabled = false))
            _completedBtnState.emit(BtnState.Complete(isEnabled = false))
            _isCompleted.emit(false)
        }
    }

    private fun isCompleteState(stateFlow: StateFlow<BtnState>): Boolean {
        return stateFlow.value is BtnState.Complete
    }
}
