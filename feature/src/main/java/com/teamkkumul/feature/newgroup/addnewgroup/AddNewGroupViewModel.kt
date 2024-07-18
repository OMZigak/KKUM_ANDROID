package com.teamkkumul.feature.newgroup.addnewgroup

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
class AddNewGroupViewModel @Inject constructor(
    private val meetingsRepository: MeetingsRepository,
) : ViewModel() {
    private val _meetingsState = MutableStateFlow<UiState<String>>(UiState.Loading)
    val meetingsState get() = _meetingsState.asStateFlow()

    fun addNewGroup(request: String) {
        viewModelScope.launch {
            meetingsRepository.addNewGroup(request)
                .onSuccess { invitationCode ->
                    if (invitationCode.isNotEmpty()) {
                        _meetingsState.emit(UiState.Success(invitationCode))
                    }
                }.onFailure {
                    _meetingsState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }
}
