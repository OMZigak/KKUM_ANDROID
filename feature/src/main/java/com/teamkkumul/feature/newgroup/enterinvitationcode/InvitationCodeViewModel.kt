package com.teamkkumul.feature.newgroup.enterinvitationcode

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MeetingsRepository
import com.teamkkumul.core.network.dto.request.RequestEnterInvitationCodeDto
import com.teamkkumul.core.network.dto.response.ResponseAddNewGroupDto
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
    private val _meetingsState = MutableStateFlow<UiState<ResponseAddNewGroupDto>>(UiState.Empty)
    val meetingsState get() = _meetingsState.asStateFlow()

    fun enterInvitationCode(request: RequestEnterInvitationCodeDto) {
        viewModelScope.launch {
            _meetingsState.emit(UiState.Loading)
            meetingsRepository.enterInvitationCode(request)
                .onSuccess {
                    //_meetingsState.emit(UiState.Success(response.data))
                    Log.e("EnterInvitationCode", "성공")
                }.onFailure {
                    //_meetingsState.emit(UiState.Failure(it))
                    Log.e("EnterInvitationCode", "실패")
                }
        }
    }
}
