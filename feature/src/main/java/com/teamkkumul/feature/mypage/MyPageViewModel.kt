package com.teamkkumul.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.HomeRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.home.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _myPageState = MutableStateFlow<UiState<UserModel>>(UiState.Loading)
    val myPageState: StateFlow<UiState<UserModel>> = _myPageState.asStateFlow()

    fun getMyPageUserInfo() {
        viewModelScope.launch {
            homeRepository.getUserInfo()
                .onSuccess {
                    if (it.profileImg == null) {
                        _myPageState.emit(UiState.Empty)
                    } else {
                        _myPageState.emit(UiState.Success(it))
                    }
                }.onFailure {
                    _myPageState.emit(UiState.Failure(it.message.toString()))
                }
        }
    }
}
