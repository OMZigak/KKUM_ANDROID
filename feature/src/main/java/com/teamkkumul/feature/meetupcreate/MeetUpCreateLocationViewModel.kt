package com.teamkkumul.feature.meetupcreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MeetUpCreateLocationRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.MeetUpCreateLocationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetUpCreateLocationViewModel @Inject constructor(
    private val meetUpCreateLocationRepository: MeetUpCreateLocationRepository,
) : ViewModel() {

    private val _meetUpCreateLocationState =
        MutableStateFlow<UiState<List<MeetUpCreateLocationModel.Location>>>(UiState.Loading)
    val meetUpCreateLocationState get() = _meetUpCreateLocationState.asStateFlow()
    fun getMeetUpCreateLocation(q: String) = viewModelScope.launch {
        meetUpCreateLocationRepository.getMeetUpCreateLocation(q)
            .onSuccess { meetUpCreateLocationRepository ->
                _meetUpCreateLocationState.emit(UiState.Success(meetUpCreateLocationRepository))
            }.onFailure { exception ->
                _meetUpCreateLocationState.emit(UiState.Failure(exception.message.toString()))
            }
    }

    fun setEmptyLocationList() {
        viewModelScope.launch {
            _meetUpCreateLocationState.emit(UiState.Loading)
        }
    }
}
