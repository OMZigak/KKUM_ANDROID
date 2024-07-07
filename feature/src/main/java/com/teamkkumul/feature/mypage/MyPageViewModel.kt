package com.teamkkumul.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.UserInfoRepository
import com.teamkkumul.core.ui.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _userInfoState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val userInfoState get() = _userInfoState.asStateFlow()

    fun saveUserToken(token: String) {
        viewModelScope.launch {
            userInfoRepository.saveAccessToken(token)
        }
    }

    fun getUserToken() {
        viewModelScope.launch {
            userInfoRepository.getAccessToken().collectLatest {
                _userInfoState.emit(UiState.Loading)
                if (it.isNotEmpty()) {
                    _userInfoState.emit(UiState.Success(it))
                } else {
                    _userInfoState.emit(UiState.Failure(it))
                }
            }
        }
    }

    fun deleteToken() {
        viewModelScope.launch {
            userInfoRepository.clearAll()
        }
    }
}
