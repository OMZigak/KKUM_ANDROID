package com.teamkkumul.feature.newgroup.addnewgroup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MeetingsRepository
import com.teamkkumul.core.network.api.MeetingsService
import com.teamkkumul.core.network.dto.request.RequestAddNewGroupDto
import com.teamkkumul.core.network.dto.response.ResponseAddNewGroupDto
import com.teamkkumul.core.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AddNewGroupViewModel @Inject constructor(
    private val meetingsRepository: MeetingsRepository,
) : ViewModel() {

    private val _meetingsState = MutableStateFlow<UiState<ResponseAddNewGroupDto>>(UiState.Empty)
    val meetingsState get() = _meetingsState.asStateFlow()

    private fun addNewGroup(request: RequestAddNewGroupDto) {
        viewModelScope.launch {
            _meetingsState.emit(UiState.Loading)
            meetingsRepository.addNewGroup(request)
                .onSuccess {
                    if (it.isNotEmpty()) _meetingsState.emit(UiState.Success(it))
                }.onFailure {
                    _meetingsState.emit(UiState.Failure(it.message()))
                }
        }
    }
}
