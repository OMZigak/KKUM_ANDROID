package com.teamkkumul.feature.meetup.readystatus.readyinfoinput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.feature.utils.TimeStorage.END_HOUR
import com.teamkkumul.feature.utils.TimeStorage.END_MINUTE
import com.teamkkumul.feature.utils.TimeStorage.START_TIME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReadyInfoInputViewModel : ViewModel() {

    private val _readyInputState = MutableStateFlow(false)
    val readyInputState: StateFlow<Boolean> get() = _readyInputState

    private val _readyHour = MutableStateFlow(true)
    val readyHour: StateFlow<Boolean> get() = _readyHour

    private val _readyMinute = MutableStateFlow(true)
    val readyMinute: StateFlow<Boolean> get() = _readyMinute

    private val _movingHour = MutableStateFlow(true)
    val movingHour: StateFlow<Boolean> get() = _movingHour

    private val _movingMinute = MutableStateFlow(true)
    val movingMinute: StateFlow<Boolean> get() = _movingMinute

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
            val isFormValid =
                _readyHour.value &&
                    _readyMinute.value &&
                    _movingHour.value &&
                    _movingMinute.value

            _readyInputState.emit(isFormValid)
        }
    }
}
