package com.teamkkumul.feature.utils.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MeetUpRepository
import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.core.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteDialogViewModel @Inject constructor(
    private val meetUpRepository: MeetUpRepository,
    private val myGroupRepository: MyGroupRepository,
) : ViewModel() {
    private val _deleteMyGroupState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val deleteMyGroupState get() = _deleteMyGroupState.asSharedFlow()

    /*  fun deleteMeetUp(meetUpId: Int) {
          viewModelScope.launch {
              meetUpRepository.deleteMeetUp(meetUpId)
                  .onSuccess {

                  }
                  .onFailure {
                  }
          }
      }*/

    fun deleteMyGroup(meetingId: Int) {
        viewModelScope.launch {
            myGroupRepository.deleteMyGroup(meetingId).onSuccess {
                _deleteMyGroupState.emit(UiState.Success(Unit))
            }
                .onFailure { exception ->
                    _deleteMyGroupState.emit(UiState.Failure(exception.message.toString()))
                }
        }
    }
}
