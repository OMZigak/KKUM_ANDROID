package com.teamkkumul.feature.meetup.readystatus.readyinfoinput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.HomeRepository
import com.teamkkumul.core.data.repository.MeetUpRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.meetup.readystatus.readyinfoinput.model.ReadyInfo
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadyInfoInputViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val meetUpRepository: MeetUpRepository,
) : ViewModel() {
    private val _readyInputState = MutableStateFlow(false)
    val readyInputState: StateFlow<Boolean> get() = _readyInputState

    private val _readyInfo = MutableStateFlow(ReadyInfo())
    val readyInfo: StateFlow<ReadyInfo> get() = _readyInfo.asStateFlow()

    private val _patchReadyInfoInputState = MutableSharedFlow<UiState<Boolean>>()
    val patchReadyInfoInputState: SharedFlow<UiState<Boolean>> get() = _patchReadyInfoInputState

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
        _readyInfo.update { currentInfo ->
            currentInfo.copy(readyHour = isReadyHourValid(input))
        }
        validateForm()
    }

    fun setReadyMinute(input: String) {
        _readyInfo.update { currentInfo ->
            currentInfo.copy(readyMinute = isReadyMinuteValid(input))
        }
        validateForm()
    }

    fun setMovingHour(input: String) {
        _readyInfo.update { currentInfo ->
            currentInfo.copy(movingHour = isReadyHourValid(input))
        }
        validateForm()
    }

    fun setMovingMinute(input: String) {
        _readyInfo.update { currentInfo ->
            currentInfo.copy(movingMinute = isReadyMinuteValid(input))
        }
        validateForm()
    }

    private fun isReadyHourValid(input: String): Boolean =
        input.toInt() in START_TIME..END_HOUR

    private fun isReadyMinuteValid(input: String): Boolean =
        input.toInt() in START_TIME..END_MINUTE

    private fun validateForm() {
        val isFormValid = (_readyInfo.value.readyHour == true) &&
            (_readyInfo.value.readyMinute == true) &&
            (_readyInfo.value.movingHour == true) &&
            (_readyInfo.value.movingMinute == true)
        _readyInputState.update { isFormValid }
    }
}
