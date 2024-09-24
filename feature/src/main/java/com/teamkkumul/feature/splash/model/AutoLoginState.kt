package com.teamkkumul.feature.splash.model

sealed interface AutoLoginState {
    data object NavigateToLogin : AutoLoginState
    data object NavigateToOnBoarding : AutoLoginState
    data object NavigateToMain : AutoLoginState
    data object Loading : AutoLoginState
}
