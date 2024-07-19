package com.teamkkumul.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.UserInfoRepository
import com.teamkkumul.feature.splash.model.AutoLoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _loginState: MutableStateFlow<AutoLoginState> =
        MutableStateFlow(AutoLoginState.Loading)
    val loginState: StateFlow<AutoLoginState> get() = _loginState.asStateFlow()

    private val _refreshToken: MutableStateFlow<String> = MutableStateFlow("")

    private val _userName: MutableStateFlow<String> = MutableStateFlow("")

    init {
        observeUserState()
        getRefreshToken()
        getUserName()
    }

    private fun observeUserState() {
        viewModelScope.launch {
            combine(_refreshToken, _userName) { refreshToken, userName ->
                when {
                    refreshToken.isEmpty() -> AutoLoginState.NavigateToLogin

                    refreshToken.isNotEmpty() && userName.isEmpty() -> AutoLoginState.NavigateToOnBoarding

                    refreshToken.isNotEmpty() && userName.isNotEmpty() -> AutoLoginState.NavigateToMain

                    else -> AutoLoginState.Loading
                }
            }.collectLatest { state ->
                _loginState.emit(state)
            }
        }
    }

    private fun getRefreshToken() {
        viewModelScope.launch {
            userInfoRepository.getRefreshToken().collectLatest {
                _refreshToken.emit(it)
            }
        }
    }

    private fun getUserName() {
        viewModelScope.launch {
            userInfoRepository.getMemberName().collectLatest {
                _userName.emit(it)
            }
        }
    }
}
