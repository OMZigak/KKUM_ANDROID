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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _loginState: MutableStateFlow<AutoLoginState> =
        MutableStateFlow(AutoLoginState.Loading)
    val loginState: StateFlow<AutoLoginState> get() = _loginState.asStateFlow()

    init {
        getUserToken()
    }

    private fun getUserToken() {
        viewModelScope.launch {
            userInfoRepository.getAccessToken().collectLatest {
                if (it.isNotEmpty()) {
                    _loginState.emit(AutoLoginState.NavigateToMain)
                } else {
                    _loginState.emit(AutoLoginState.NavigateToLogin)
                }
            }
        }
    }
}
