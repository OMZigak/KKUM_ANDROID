package com.teamkkumul.feature.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.ReqresRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.compose.model.ExampleSideEffect
import com.teamkkumul.feature.compose.model.ExampleState
import com.teamkkumul.model.example.ReqresModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleComposeViewModel @Inject constructor(
    private val reqresRepository: ReqresRepository,
) : ViewModel() {
    private val _reqresUserState: MutableStateFlow<ExampleState> = MutableStateFlow(ExampleState())
    val reqresUserState: StateFlow<ExampleState>
        get() = _reqresUserState.asStateFlow()

    private val _sideEffect: MutableSharedFlow<ExampleSideEffect> = MutableSharedFlow()
    val sideEffect: SharedFlow<ExampleSideEffect>
        get() = _sideEffect.asSharedFlow()

    fun getReqresUserList(page: Int) {
        viewModelScope.launch {
            reqresRepository.getReqresList(page)
                .onSuccess { handleSuccess(it) }
                .onFailure { handleError(it) }
        }
    }

    private suspend fun handleSuccess(data: List<ReqresModel>) {
        if (data.isNotEmpty()) {
            _reqresUserState.value =
                _reqresUserState.value.copy(state = UiState.Success(data.toPersistentList()))
        } else {
            _reqresUserState.value = _reqresUserState.value.copy(state = UiState.Empty)
            _sideEffect.emit(ExampleSideEffect.NavigateToEmpty)
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        _sideEffect.emit(ExampleSideEffect.ShowSnackBar(throwable.message.toString()))
    }
}
