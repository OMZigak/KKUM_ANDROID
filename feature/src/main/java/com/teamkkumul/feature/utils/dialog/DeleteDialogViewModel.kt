package com.teamkkumul.feature.utils.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MeetUpRepository
import com.teamkkumul.core.data.repository.MyGroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteDialogViewModel @Inject constructor(
    private val meetUpRepository: MeetUpRepository,
    private val myGroupRepository: MyGroupRepository,
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

    fun deleteMyGroup(meetingId: Int) {
        viewModelScope.launch {
            myGroupRepository.deleteMyGroup(meetingId).onSuccess {
            }
        }
    }
}
