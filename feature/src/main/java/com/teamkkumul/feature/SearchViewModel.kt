package com.teamkkumul.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.ReqresRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.example.ReqresModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val reqresRepository: ReqresRepository,
) : ViewModel() {
    private val _reqresUserState = MutableStateFlow<UiState<List<ReqresModel>>>(UiState.Loading)
    val reqresUserState get() = _reqresUserState.asStateFlow()

    init {
        getReqresUserList(1)
    }

    private fun getReqresUserList(page: Int) {
        viewModelScope.launch {
            _reqresUserState.emit(UiState.Loading)
            reqresRepository.getReqresList(page)
                .onSuccess {
                    if (it.isNotEmpty()) _reqresUserState.emit(UiState.Success(it.toList()))
                }.onFailure {
                    _reqresUserState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }
}
