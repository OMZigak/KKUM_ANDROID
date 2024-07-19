package com.teamkkumul.feature.mygroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.core.data.repository.UserInfoRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.MyGroupModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGroupViewModel @Inject constructor(
    private val myGroupRepository: MyGroupRepository,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {

    private val _myGroupState = MutableStateFlow<UiState<MyGroupModel>>(UiState.Loading)
    val myGroupState get() = _myGroupState.asStateFlow()

    private val _myGroupListState =
        MutableStateFlow<UiState<List<MyGroupModel.Meeting>>>(UiState.Loading)
    val myGroupListState get() = _myGroupListState.asStateFlow()

    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    fun getName() {
        viewModelScope.launch {
            userInfoRepository.getMemberName().collectLatest {
                _userName.value = it
            }
        }
    }

    fun getMyGroupList() = viewModelScope.launch {
        myGroupRepository.getMyGroup()
            .onSuccess { myGroupModel ->
                if (myGroupModel.meetings.isEmpty()) {
                    _myGroupListState.emit(UiState.Empty)
                } else {
                    _myGroupListState.emit(UiState.Success(myGroupModel.meetings))
                }
                _myGroupState.emit(UiState.Success(myGroupModel))
            }
            .onFailure { exception ->
                _myGroupState.emit(UiState.Failure(exception.message.toString()))
            }
    }
}
