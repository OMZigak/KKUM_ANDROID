package com.teamkkumul.feature.meetup.lateperson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MeetUpRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.LatePersonModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LatePersonViewModel @Inject constructor(
    private val meetUpRepository: MeetUpRepository,
) : ViewModel() {

    private val _latePersonState = MutableStateFlow<UiState<LatePersonModel>>(UiState.Loading)
    val latePersonState get() = _latePersonState.asStateFlow()

    private val _patchMeetUpState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val patchMeetUpState get() = _patchMeetUpState.asStateFlow()

    fun getLateComersList(promiseId: Int) {
        viewModelScope.launch {
            meetUpRepository.getLateComersList(promiseId)
                .onSuccess {
                    _latePersonState.emit(UiState.Success(it))
                }.onFailure { exception ->
                    _latePersonState.emit(UiState.Failure(exception.message.toString()))
                }
        }
    }

    fun patchMeetUpComplete(promiseId: Int) {
        viewModelScope.launch {
            _patchMeetUpState.emit(UiState.Loading)
            meetUpRepository.patchMeetUpComplete(promiseId)
                .onSuccess {
                    _patchMeetUpState.emit(UiState.Success(Unit))
                }.onFailure { exception ->
                    _patchMeetUpState.emit(UiState.Failure(exception.message.toString()))
                }
        }
    }
}
