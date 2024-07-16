package com.teamkkumul.feature.mygroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.MyGroupModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGroupViewModel @Inject constructor(
    private val myGroupRepository: MyGroupRepository,
) : ViewModel() {

    private val _myGroupState = MutableStateFlow<UiState<MyGroupModel>>(UiState.Loading)
    val myGroupState get() = _myGroupState.asStateFlow()

    fun getMyGroupList() = viewModelScope.launch {
        _myGroupState.emit(UiState.Loading)
        myGroupRepository.getMyGroupList()
            .onSuccess { myGroupModel ->
                if (myGroupModel.meetings.isEmpty()) {
                    _myGroupState.emit(UiState.Empty)
                } else {
                    _myGroupState.emit(UiState.Success(myGroupModel))
                }
            }
            .onFailure { exception ->
                _myGroupState.emit(UiState.Failure(exception.message.toString()))
            }
    }
}
