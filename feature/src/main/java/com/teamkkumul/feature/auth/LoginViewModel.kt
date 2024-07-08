package com.teamkkumul.feature.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.teamkkumul.core.data.repository.UserInfoRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.auth.model.KakaoLoginState
import com.teamkkumul.feature.auth.model.LoginSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _loginState: MutableStateFlow<KakaoLoginState> =
        MutableStateFlow(KakaoLoginState())
    val loginState: StateFlow<KakaoLoginState> get() = _loginState.asStateFlow()

    private val _loginSideEffect: MutableSharedFlow<LoginSideEffect> = MutableSharedFlow()
    val loginSideEffect: SharedFlow<LoginSideEffect>
        get() = _loginSideEffect.asSharedFlow()

    fun getUserToken() {
        viewModelScope.launch {
            userInfoRepository.getAccessToken().collectLatest {
                if (it.isNotEmpty()) {
                    _loginState.value =
                        _loginState.value.copy(state = UiState.Success(true))
                    _loginSideEffect.emit(LoginSideEffect.NavigateToMain)
                } else {
                    _loginState.value =
                        _loginState.value.copy(state = UiState.Failure("fail"))
                    _loginSideEffect.emit(LoginSideEffect.ShowSnackBar("로그인이 필요합니다"))
                }
            }
        }
    }

    private fun saveAccessToken(token: String) {
        viewModelScope.launch {
            userInfoRepository.saveAccessToken(token)
        }
    }

    fun startKaKaoLogin(context: Context) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            // 카카오톡으로 로그인
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                handleKaKaoLoginResult(token, error)
            }
        } else {
            // 카카오 계정으로 로그인
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                handleKaKaoLoginResult(token, error)
            }
        }
    }

    private fun handleKaKaoLoginResult(token: OAuthToken?, error: Throwable?) {
        viewModelScope.launch {
            when {
                token != null -> kakaoLoginSuccess(token)
                error != null -> kakaoLoginFailure(error)
            }
        }
    }

    private fun kakaoLoginFailure(error: Throwable) {
        viewModelScope.launch {
            _loginState.value =
                _loginState.value.copy(state = UiState.Failure("fail"))
            when {
                error is ClientError && error.reason == ClientErrorCause.Cancelled ->
                    _loginSideEffect.emit(LoginSideEffect.ShowSnackBar("카카오 로그인이 취소되었습니다"))

                else -> _loginSideEffect.emit(LoginSideEffect.ShowSnackBar("카카오 로그인에 실패했습니다"))
            }
        }
    }

    private fun kakaoLoginSuccess(token: OAuthToken) {
        viewModelScope.launch {
            _loginState.value =
                _loginState.value.copy(state = UiState.Success(true))
            saveAccessToken(token.accessToken)
            _loginSideEffect.emit(LoginSideEffect.NavigateToMain)
        }
    }

    fun deleteToken() {
        viewModelScope.launch {
            userInfoRepository.clearAll()
        }
    }
}
