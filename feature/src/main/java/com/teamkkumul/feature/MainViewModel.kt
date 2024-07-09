package com.teamkkumul.feature

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
class MainViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _userInfoState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val userInfoState get() = _userInfoState.asStateFlow()

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            userInfoRepository.getAccessToken().collectLatest {
                if (it.isNotEmpty()) {
                    _userInfoState.emit(UiState.Success(true))
                } else {
                    _userInfoState.emit(UiState.Failure("fail"))
                }
            }
        }
    }
}
