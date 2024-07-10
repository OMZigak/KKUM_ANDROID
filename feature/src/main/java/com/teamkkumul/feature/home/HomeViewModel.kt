package com.teamkkumul.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.feature.home.model.BtnState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _readyBtnState =
        MutableStateFlow<BtnState>(BtnState.Default(isEnabled = true))
    val readyBtnState: StateFlow<BtnState> get() = _readyBtnState

    private val _movingStartBtnState =
        MutableStateFlow<BtnState>(BtnState.DefaultGray(isEnabled = false))
    val movingStartBtnState: StateFlow<BtnState> get() = _movingStartBtnState

    private val _completedBtnState =
        MutableStateFlow<BtnState>(BtnState.DefaultGray(isEnabled = false))
    val completedBtnState: StateFlow<BtnState> get() = _completedBtnState

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
}
