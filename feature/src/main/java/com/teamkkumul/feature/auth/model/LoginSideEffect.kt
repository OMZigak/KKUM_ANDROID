package com.teamkkumul.feature.auth.model

sealed interface LoginSideEffect {
    data object NavigateToMain : LoginSideEffect
    data class ShowSnackBar(val message: String) : LoginSideEffect
}
