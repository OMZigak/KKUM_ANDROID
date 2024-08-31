package com.teamkkumul.feature.meetup.readystatus.readyinfoinput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.HomeRepository
import com.teamkkumul.core.data.repository.MeetUpRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.utils.TimeStorage.END_HOUR
import com.teamkkumul.feature.utils.TimeStorage.END_MINUTE
import com.teamkkumul.feature.utils.TimeStorage.START_TIME
import com.teamkkumul.model.MeetUpDetailModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadyInfoInputViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val meetUpRepository: MeetUpRepository,
) : ViewModel() {
    private val _readyInputState = MutableStateFlow(false)
    val readyInputState: StateFlow<Boolean> get() = _readyInputState

    private val _readyHour = MutableSharedFlow<Boolean>()
    val readyHour: SharedFlow<Boolean> get() = _readyHour

    private val _readyMinute = MutableSharedFlow<Boolean>()
    val readyMinute: SharedFlow<Boolean> get() = _readyMinute

    private val _movingHour = MutableSharedFlow<Boolean>()
    val movingHour: SharedFlow<Boolean> get() = _movingHour

    private val _movingMinute = MutableSharedFlow<Boolean>()
    val movingMinute: SharedFlow<Boolean> get() = _movingMinute

    private val _patchReadyInfoInputState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val patchReadyInfoInputState: StateFlow<UiState<Boolean>> get() = _patchReadyInfoInputState

    private val _meetUpDetailState = MutableStateFlow<UiState<MeetUpDetailModel>>(UiState.Loading)
    val meetUpDetailState = _meetUpDetailState.asStateFlow()

    fun getMeetUpDetail(promiseId: Int) {
        viewModelScope.launch {
            meetUpRepository.getMeetUpDetail(promiseId).onSuccess {
                _meetUpDetailState.emit(UiState.Success(it))
            }.onFailure {
                _meetUpDetailState.emit(UiState.Failure(it.message.toString()))
            }
        }
    }

    fun patchReadyInfoInput(promiseId: Int, readyTime: Int, movingTime: Int) {
        viewModelScope.launch {
            homeRepository.patchReadyInfoInput(
                promiseId = promiseId,
                preparationTime = readyTime,
                travelTime = movingTime,
            ).onSuccess {
                _patchReadyInfoInputState.emit(UiState.Success(true))
            }.onFailure {
                _patchReadyInfoInputState.emit(UiState.Failure(it.message.toString()))
            }
        }
    }

    fun setReadyHour(input: String) {
        viewModelScope.launch {
            _readyHour.emit(isReadyHourValid(input))
            validateForm()
        }
    }

    fun setReadyMinute(input: String) {
        viewModelScope.launch {
            _readyMinute.emit(isReadyMinuteValid(input))
            validateForm()
        }
    }

    fun setMovingHour(input: String) {
        viewModelScope.launch {
            _movingHour.emit(isReadyHourValid(input))
            validateForm()
        }
    }

    fun setMovingMinute(input: String) {
        viewModelScope.launch {
            _movingMinute.emit(isReadyMinuteValid(input))
            validateForm()
        }
    }

    private fun isReadyHourValid(input: String): Boolean =
        input.toIntOrNull() in START_TIME..END_HOUR

    private fun isReadyMinuteValid(input: String): Boolean =
        input.toIntOrNull() in START_TIME..END_MINUTE

    private fun validateForm() {
        viewModelScope.launch {
            combine(
                _readyHour,
                _readyMinute,
                _movingHour,
                _movingMinute,
            ) { readyHour, readyMinute, movingHour, movingMinute ->
                readyHour && readyMinute && movingHour && movingMinute
            }.collectLatest { isFormValid ->
                _readyInputState.emit(isFormValid)
            }
        }
    }
}
