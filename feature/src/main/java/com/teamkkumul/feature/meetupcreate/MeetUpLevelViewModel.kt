package com.teamkkumul.feature.meetupcreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MeetUpCreateLocationRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.MeetUpCreateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MeetUpLevelViewModel @Inject constructor(
    private val meetUpCreateLocationRepository: MeetUpCreateLocationRepository,
) : ViewModel() {

    private val _meetUpCreateState = MutableSharedFlow<UiState<Int>>()
    val meetUpCreateState get() = _meetUpCreateState.asSharedFlow()

    private val _meetUpEditState = MutableSharedFlow<UiState<Int>>()
    val meetUpEditState get() = _meetUpEditState.asSharedFlow()

    fun postMeetUpCreate(meetingId: Int, meetUpCreateModel: MeetUpCreateModel) {
        viewModelScope.launch {
            meetUpCreateLocationRepository.postMeetUpCreate(meetingId, meetUpCreateModel)
                .onSuccess {
                    _meetUpCreateState.emit(UiState.Success(it))
                }.onFailure {
                    _meetUpCreateState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }

    fun putMeetUpEdit(promiseId: Int, meetUpCreateModel: MeetUpCreateModel) {
        viewModelScope.launch {
            meetUpCreateLocationRepository.putMeetUpEdit(promiseId, meetUpCreateModel)
                .onSuccess {
                    _meetUpEditState.emit(UiState.Success(it))
                }.onFailure {
                    _meetUpEditState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }
}
