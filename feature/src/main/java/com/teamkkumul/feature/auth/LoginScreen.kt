package com.teamkkumul.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teamkkumul.core.designsystem.theme.Gray8
import com.teamkkumul.core.designsystem.theme.KkumulAndroidTheme
import com.teamkkumul.core.designsystem.theme.KkumulTheme
import com.teamkkumul.core.designsystem.theme.Yellow
import com.teamkkumul.feature.R

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getUserToken()
    }

    LoginScreen(
        onLoginBtnClick = {
            viewModel.startKaKaoLogin(context)
        },
    )

    /*LaunchedEffect(lifecycleOwner) {
        viewModel.isAutoLoginState.flowWithLifecycle(lifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> navigateTo<MainActivity>(context)
                    else -> Unit
                }
            }.launchIn(lifecycleOwner.lifecycleScope)
    }*/
}

@Composable
fun LoginScreen(
    onLoginBtnClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 160.dp),
    ) {
        Button(
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Yellow),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 20.dp),
            onClick = { onLoginBtnClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_kakao_black_24),
                    contentDescription = "kakao",
                    modifier = Modifier.align(Alignment.CenterStart),
                )
                Text(
                    text = stringResource(R.string.login_kakao_btn_text),
                    style = KkumulTheme.typography.body03,
                    color = Gray8,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KkumulAndroidTheme {
        LoginScreen(
            onLoginBtnClick = {},
        )
    }
}
