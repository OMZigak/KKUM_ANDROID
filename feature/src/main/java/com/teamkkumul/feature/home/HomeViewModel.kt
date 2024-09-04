package com.teamkkumul.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.HomeRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.utils.model.BtnState
import com.teamkkumul.feature.utils.type.ReadyBtnTextType
import com.teamkkumul.model.home.HomeReadyStatusModel
import com.teamkkumul.model.home.HomeTodayMeetingModel
import com.teamkkumul.model.home.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _readyBtnState =
        MutableStateFlow<BtnState>(
            BtnState.Default(
                isEnabled = true,
                btnText = ReadyBtnTextType.READY_DEFAULT,
            ),
        )
    val readyBtnState: StateFlow<BtnState> get() = _readyBtnState

    private val _movingBtnState =
        MutableStateFlow<BtnState>(
            BtnState.DefaultGray(
                isEnabled = false,
                btnText = ReadyBtnTextType.MOVING_DEFAULT,
            ),
        )
    val movingStartBtnState: StateFlow<BtnState> get() = _movingBtnState

    private val _completedBtnState =
        MutableStateFlow<BtnState>(
            BtnState.DefaultGray(
                isEnabled = false,
                btnText = ReadyBtnTextType.COMPLETED,
            ),
        )
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

    private val _isReady = MutableStateFlow<Boolean>(false)
    val isReady: StateFlow<Boolean> get() = _isReady.asStateFlow()

    private val _isMoving = MutableStateFlow(false)
    val isMoving: StateFlow<Boolean> get() = _isMoving

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> get() = _isCompleted

    fun updateReadyHelpText() {
        viewModelScope.launch {
            _isReady.update { true }
        }
    }

    fun updateAllInvisible() {
        viewModelScope.launch {
            _isReady.update { false }
            _isMoving.update { false }
            _isCompleted.update { false }
        }
    }

    fun getReadyStatus(promiseId: Int) {
        viewModelScope.launch {
            homeRepository.getReadyStatus(promiseId)
                .onSuccess {
                    if (it == null) {
                        _readyStatusState.emit(UiState.Empty)
                    } else {
                        _readyStatusState.emit(UiState.Success(it))
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
            _readyBtnState.emit(
                BtnState.InProgress(
                    isEnabled = false,
                    btnText = ReadyBtnTextType.READY_START,
                ),
            )
            _movingBtnState.emit(
                BtnState.Default(
                    isEnabled = true,
                    btnText = ReadyBtnTextType.MOVING_DEFAULT,
                ),
            )
            _completedBtnState.emit(
                BtnState.DefaultGray(
                    isEnabled = false,
                    btnText = ReadyBtnTextType.COMPLETED,
                ),
            )
            _isReady.emit(false)
            _isMoving.emit(true)
        }
    }

    fun clickMovingStartBtn() {
        viewModelScope.launch {
            if (isCompleteState(_movingBtnState)) return@launch
            _readyBtnState.emit(
                BtnState.Complete(
                    isEnabled = false,
                    btnText = ReadyBtnTextType.READY_COMPLETE,
                ),
            )
            _movingBtnState.emit(
                BtnState.InProgress(
                    isEnabled = false,
                    btnText = ReadyBtnTextType.MOVING_START,
                ),
            )
            _completedBtnState.emit(
                BtnState.Default(
                    isEnabled = true,
                    btnText = ReadyBtnTextType.COMPLETED,
                ),
            )
            _isMoving.emit(false)
            _isCompleted.emit(true)
        }
    }

    fun clickCompletedBtn() {
        viewModelScope.launch {
            if (isCompleteState(_completedBtnState)) return@launch
            _readyBtnState.emit(
                BtnState.Complete(
                    isEnabled = false,
                    btnText = ReadyBtnTextType.READY_COMPLETE,
                ),
            )
            _movingBtnState.emit(
                BtnState.Complete(
                    isEnabled = false,
                    btnText = ReadyBtnTextType.MOVING_COMPLETE,
                ),
            )
            _completedBtnState.emit(
                BtnState.Complete(
                    isEnabled = false,
                    btnText = ReadyBtnTextType.COMPLETED,
                ),
            )
            _isCompleted.emit(false)
            _isReady.emit(false)
        }
    }

    private fun isCompleteState(stateFlow: StateFlow<BtnState>): Boolean {
        return stateFlow.value is BtnState.Complete
    }
}
