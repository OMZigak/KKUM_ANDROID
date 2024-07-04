package com.teamkkumul.feature.compose.model

import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.example.ReqresModel
import kotlinx.collections.immutable.ImmutableList

data class ExampleState(
    val state: UiState<ImmutableList<ReqresModel>> = UiState.Loading,
)
