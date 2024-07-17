package com.teamkkumul.feature.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.teamkkumul.core.data.repository.LoginRepository
import com.teamkkumul.core.data.repository.UserInfoRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.auth.model.KakaoLoginState
import com.teamkkumul.feature.auth.model.LoginSideEffect
import com.teamkkumul.feature.utils.KeyStorage.DATA_NULL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val loginRepository: LoginRepository,
) : ViewModel() {
    private val _kakaoLoginState: MutableStateFlow<KakaoLoginState> =
        MutableStateFlow(KakaoLoginState())
    val kakaoLoginState: StateFlow<KakaoLoginState> get() = _kakaoLoginState.asStateFlow()

    private val _loginSideEffect: MutableSharedFlow<LoginSideEffect> = MutableSharedFlow()
    val loginSideEffect: SharedFlow<LoginSideEffect>
        get() = _loginSideEffect.asSharedFlow()

    private val _fcmToken: MutableStateFlow<String> = MutableStateFlow("")
    private val _kakaoToken: MutableStateFlow<String> = MutableStateFlow("")

    private fun saveRefreshToken(token: String) {
        viewModelScope.launch {
            userInfoRepository.saveRefreshToken(token)
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
                token != null -> kakaoLoginSuccess(token) // 성공
                error != null -> kakaoLoginFailure(error) // 실패
            }
        }
    }

    private fun kakaoLoginFailure(error: Throwable) {
        viewModelScope.launch {
            _kakaoLoginState.value =
                _kakaoLoginState.value.copy(state = UiState.Failure("fail"))
            when {
                error is ClientError && error.reason == ClientErrorCause.Cancelled ->
                    _loginSideEffect.emit(LoginSideEffect.ShowSnackBar("카카오 로그인이 취소되었습니다"))

                else -> _loginSideEffect.emit(LoginSideEffect.ShowSnackBar("카카오 로그인에 실패했습니다"))
            }
        }
    }

    private fun kakaoLoginSuccess(token: OAuthToken) {
        viewModelScope.launch {
            saveAccessToken(token.accessToken)
            _kakaoToken.value = token.accessToken
            Timber.tag("kakao").d("accessToken: ${token.accessToken}")
            postLogin()
        }
    }

    private suspend fun saveAccessToken(token: String) {
        userInfoRepository.saveAccessToken(token)
    }

    fun setFcmToken(token: String) {
        _fcmToken.value = token
    }

    private fun saveMemberName(input: String?) {
        viewModelScope.launch {
            if (input != null && input != DATA_NULL) {
                userInfoRepository.saveMemberName(input)
            }
        }
    }

    private fun saveIsAutoLogin(input: Boolean) {
        viewModelScope.launch {
            userInfoRepository.saveAutoLogin(input)
        }
    }

    private fun postLogin() {
        viewModelScope.launch {
            loginRepository.postLogin("KAKAO", _fcmToken.value, _kakaoToken.value)
                .onSuccess { response ->
                    saveAccessToken(response.accessToken)
                    saveRefreshToken(response.refreshToken)
                    saveMemberName(response.name)
                    saveIsAutoLogin(true)
                    _loginSideEffect.emit(LoginSideEffect.NavigateToOnBoarding)
                }.onFailure {
                    _loginSideEffect.emit(LoginSideEffect.ShowSnackBar(it.message.toString()))
                }
        }
    }
}
