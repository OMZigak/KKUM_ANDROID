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

    private val _myGroupListState =
        MutableStateFlow<UiState<List<MyGroupModel.Meeting>>>(UiState.Loading)
    val myGroupListState get() = _myGroupListState.asStateFlow()

    // mygroupmodel은 일단 다 보냄

    fun getMyGroupList() = viewModelScope.launch {
        myGroupRepository.getMyGroup()
            .onSuccess { myGroupModel ->
                // my group model 안에 있는 list를 처리할 때 empty 화면이 있다면.. 이라는 뜻
                if (myGroupModel.meetings.isEmpty()) {
                    _myGroupListState.emit(UiState.Empty)
                } else {
                    // 성공했다면 list를 여기에 보냄
                    _myGroupListState.emit(UiState.Success(myGroupModel.meetings))
                }
                // my group model 안에 있는 count는 이렇게 보냄
                _myGroupState.emit(UiState.Success(myGroupModel))
            }
            .onFailure { exception ->
                _myGroupState.emit(UiState.Failure(exception.message.toString()))
            }
    }
}
