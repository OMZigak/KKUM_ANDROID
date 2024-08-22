package com.teamkkumul.feature.utils.dialog

import androidx.lifecycle.ViewModel
import com.teamkkumul.core.data.repository.MeetUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteDialogViewModel @Inject constructor(
    private val meetUpRepository: MeetUpRepository,
) : ViewModel() {
  /*  fun deleteMeetUp(meetUpId: Int) {
        viewModelScope.launch {
            meetUpRepository.deleteMeetUp(meetUpId)
                .onSuccess {

                }
                .onFailure {
                }
        }
    }*/
}
