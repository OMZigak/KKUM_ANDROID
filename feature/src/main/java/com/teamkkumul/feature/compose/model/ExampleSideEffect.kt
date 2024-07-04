package com.teamkkumul.feature.compose.model

sealed interface ExampleSideEffect {
    data object NavigateToEmpty : ExampleSideEffect
    data class ShowSnackBar(val message: String) : ExampleSideEffect
}
