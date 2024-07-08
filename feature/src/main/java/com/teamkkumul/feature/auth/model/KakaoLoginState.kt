package com.teamkkumul.feature.auth.model

import com.teamkkumul.core.ui.view.UiState

data class KakaoLoginState(
    val state: UiState<Boolean> = UiState.Loading,
)
