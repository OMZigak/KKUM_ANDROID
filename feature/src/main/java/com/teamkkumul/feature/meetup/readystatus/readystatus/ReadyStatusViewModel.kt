package com.teamkkumul.feature.meetup.readystatus.readystatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.HomeRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.meetup.readystatus.readystatus.model.TimeTextState
import com.teamkkumul.feature.utils.model.BtnState
import com.teamkkumul.feature.utils.time.TimeUtils.isPastTime
import com.teamkkumul.feature.utils.type.ReadyBtnTextType
import com.teamkkumul.model.home.HomeMembersStatus
import com.teamkkumul.model.home.HomeReadyStatusModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadyStatusViewModel @Inject constructor(
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

    private val _readyStatusState =
        MutableStateFlow<UiState<HomeReadyStatusModel?>>(UiState.Loading)
    val readyStatusState: StateFlow<UiState<HomeReadyStatusModel?>> =
        _readyStatusState.asStateFlow()

    private val _membersReadyStatus =
        MutableStateFlow<UiState<List<HomeMembersStatus.Participant?>>>(UiState.Loading)
    val membersReadyStatus get() = _membersReadyStatus.asStateFlow()

    private val _popUpVisible = MutableStateFlow<Boolean>(false)
    val popUpVisible get() = _popUpVisible.asStateFlow()

    private val _readyPatchState = MutableSharedFlow<UiState<Unit>>()
    val readyPatchState: SharedFlow<UiState<Unit>> get() = _readyPatchState

    private val _timeTextState = MutableStateFlow(TimeTextState())
    val timeTextState: StateFlow<TimeTextState> = _timeTextState.asStateFlow()

    fun updateReadyTime(time: String) {
        viewModelScope.launch {
            _timeTextState.update { it.copy(readyTime = time) }
        }
    }

    fun updateMovingTime(time: String) {
        viewModelScope.launch {
            _timeTextState.update { it.copy(movingTime = time) }
        }
    }

    fun updateCompletedTime(time: String) {
        viewModelScope.launch {
            _timeTextState.update { it.copy(completedTime = time) }
        }
    }

    fun setPopUpVisible(isVisible: Boolean) {
        _popUpVisible.update { isVisible }
    }

    fun getMembersReadyStatus(promiseId: Int) {
        viewModelScope.launch {
            homeRepository.getMembersReadyStatus(promiseId)
                .onSuccess {
                    if (it == null) {
                        _membersReadyStatus.emit(UiState.Empty)
                    } else {
                        _membersReadyStatus.emit(UiState.Success(it))
                    }
                }.onFailure {
                    _membersReadyStatus.emit(UiState.Failure(it.message.toString()))
                }
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
                        checkAndSetPopupVisibility(it) // 팝업 가시성 검사
                    }
                }.onFailure {
                    _readyStatusState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }

    fun patchReady(promiseId: Int) {
        viewModelScope.launch {
            homeRepository.patchReady(promiseId = promiseId).onSuccess {
                clickReadyBtn()
                _membersReadyStatus.emit(UiState.Loading)
                getMembersReadyStatus(promiseId = promiseId)
                _readyPatchState.emit(UiState.Success(Unit))
                _readyStatusState.emit(UiState.Loading)
            }.onFailure { _readyPatchState.emit(UiState.Failure(it.message.toString())) }
        }
    }

    fun patchMoving(promiseId: Int) {
        viewModelScope.launch {
            homeRepository.patchMoving(promiseId = promiseId).onSuccess {
                clickMovingStartBtn()
                _membersReadyStatus.emit(UiState.Loading)
                getMembersReadyStatus(promiseId = promiseId)
                _readyStatusState.emit(UiState.Loading)
            }
        }
    }

    fun patchCompleted(promiseId: Int) {
        viewModelScope.launch {
            homeRepository.patchCompleted(promiseId = promiseId).onSuccess {
                clickCompletedBtn()
                _membersReadyStatus.emit(UiState.Loading)
                getMembersReadyStatus(promiseId = promiseId)
                _readyStatusState.emit(UiState.Loading)
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
        }
    }

    fun clickCompletedBtn() {
        viewModelScope.launch {
            setPopUpVisible(false)
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
        }
    }

    private fun isCompleteState(stateFlow: StateFlow<BtnState>): Boolean {
        return stateFlow.value is BtnState.Complete
    }

    // 팝업 표시 여부를 결정하는 함수
    private fun checkAndSetPopupVisibility(data: HomeReadyStatusModel) {
        if (isCompleteState(_completedBtnState) || data.preparationTime == null) {
            setPopUpVisible(false)
            return
        }

        val shouldShowPopup = isPastTime(data.preparationStartAt) ||
            isPastTime(data.departureAt) ||
            isPastTime(data.arrivalAt)

        setPopUpVisible(shouldShowPopup)
    }

    fun getPromiseTime(): String =
        (_readyStatusState.value as? UiState.Success)?.data?.promiseTime.orEmpty()
}
