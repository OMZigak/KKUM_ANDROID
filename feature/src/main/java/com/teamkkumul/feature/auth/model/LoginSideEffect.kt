package com.teamkkumul.feature.auth.model

sealed interface LoginSideEffect {
    data object NavigateToMain : LoginSideEffect
    data object NavigateToOnBoarding : LoginSideEffect
    data class ShowSnackBar(val message: String) : LoginSideEffect
}
