package com.teamkkumul.feature.compose

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.teamkkumul.core.designsystem.component.ReqresRow
import com.teamkkumul.core.designsystem.theme.KkumulAndroidTheme
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.compose.model.ExampleSideEffect
import com.teamkkumul.model.example.ReqresModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun ExampleRoute(
    viewModel: ExampleComposeViewModel = hiltViewModel(),
) {
    val state by viewModel.reqresUserState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getReqresUserList(2)
    }

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is ExampleSideEffect.ShowSnackBar -> {
                        Toast.makeText(
                            context,
                            state.message,
                            Toast.LENGTH_SHORT,
                        ).show()
                    }

                    ExampleSideEffect.NavigateToEmpty -> { /* naviage to Empty page*/
                    }
                }
            }.launchIn(lifecycleOwner.lifecycleScope)
    }

    when (state.state) {
        is UiState.Success -> {
            ExampleScreen((state.state as UiState.Success<ImmutableList<ReqresModel>>).data)
        }

        else -> Unit
    }
}

@Composable
fun ExampleScreen(data: List<ReqresModel>) {
    LazyColumn {
        items(data) { user ->
            ReqresRow(user = user)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KkumulAndroidTheme {
        ExampleScreen(
            listOf(
                ReqresModel(
                    1,
                    "first",
                    "last",
                    "email",
                    "avatar",
                ),
                ReqresModel(
                    2,
                    "first",
                    "last",
                    "email",
                    "avatar",
                ),
            ),
        )
    }
}
