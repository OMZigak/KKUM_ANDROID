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

    private val _readyHour = MutableStateFlow<Boolean?>(null)
    val readyHour: StateFlow<Boolean?> get() = _readyHour

    private val _readyMinute = MutableStateFlow<Boolean?>(null)
    val readyMinute: StateFlow<Boolean?> get() = _readyMinute

    private val _movingHour = MutableStateFlow<Boolean?>(null)
    val movingHour: StateFlow<Boolean?> get() = _movingHour

    private val _movingMinute = MutableStateFlow<Boolean?>(null)
    val movingMinute: StateFlow<Boolean?> get() = _movingMinute

    private val _patchReadyInfoInputState = MutableSharedFlow<UiState<Boolean>>()
    val patchReadyInfoInputState: SharedFlow<UiState<Boolean>> get() = _patchReadyInfoInputState

    private val _meetUpDetailState = MutableStateFlow<UiState<MeetUpDetailModel>>(UiState.Loading)
    val meetUpDetailState = _meetUpDetailState.asStateFlow()

    init {
        validateForm()
    }

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
        }
    }

    fun setReadyMinute(input: String) {
        viewModelScope.launch {
            _readyMinute.emit(isReadyMinuteValid(input))
        }
    }

    fun setMovingHour(input: String) {
        viewModelScope.launch {
            _movingHour.emit(isReadyHourValid(input))
        }
    }

    fun setMovingMinute(input: String) {
        viewModelScope.launch {
            _movingMinute.emit(isReadyMinuteValid(input))
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
                (readyHour ?: false) &&
                    (readyMinute ?: false) &&
                    (movingHour ?: false) &&
                    (movingMinute ?: false)
            }.collect { isFormValid ->
                _readyInputState.emit(isFormValid)
            }
        }
    }
}
