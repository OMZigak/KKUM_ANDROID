package com.teamkkumul.feature.meetupcreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetUpCreateViewModel @Inject constructor() : ViewModel() {

    private val _meetUpInputState = MutableStateFlow(false)
    val meetUpInputState: StateFlow<Boolean> get() = _meetUpInputState
    private val _meetUpName = MutableStateFlow<Boolean>(false)
    val meetUpName: StateFlow<Boolean> get() = _meetUpName

    fun setMeetUpName(input: Boolean) {
        viewModelScope.launch {
            _meetUpName.emit(input)
            _meetUpInputState.value = _meetUpName.value
        }
    }
}
