package com.teamkkumul.feature.newgroup.enterinvitationcode

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MeetingsRepository
import com.teamkkumul.core.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationCodeViewModel @Inject constructor(
    private val meetingsRepository: MeetingsRepository
) : ViewModel() {
    private val _meetingsState = MutableStateFlow<UiState<Int>>(UiState.Loading)
    val meetingsState get() = _meetingsState.asStateFlow()

    private fun enterInvitationCode(request: String) {
        viewModelScope.launch {
            meetingsRepository.enterInvitationCode(request)
                .onSuccess { response ->
                    _meetingsState.emit(UiState.Success(response))
                    Log.e("EnterInvitationCode", "성공")
                }.onFailure {
                    _meetingsState.emit(UiState.Failure(it.message.toString()))
                    Log.e("EnterInvitationCode", "실패")
                }
        }
    }

    fun validInput(input: String) {
        val isValid = input.length == 6
        if (isValid) {
            enterInvitationCode(input)
        } else {
            _meetingsState.value = UiState.Failure("Invalid invitation code")
        }
    }
}
