package com.teamkkumul.feature.auth

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import com.teamkkumul.core.designsystem.theme.Gray8
import com.teamkkumul.core.designsystem.theme.Green4
import com.teamkkumul.core.designsystem.theme.KkumulAndroidTheme
import com.teamkkumul.core.designsystem.theme.KkumulTheme
import com.teamkkumul.core.designsystem.theme.Yellow
import com.teamkkumul.core.ui.util.context.toast
import com.teamkkumul.core.ui.util.intent.navigateTo
import com.teamkkumul.feature.MainActivity
import com.teamkkumul.feature.R
import com.teamkkumul.feature.auth.model.LoginSideEffect
import com.teamkkumul.feature.signup.SetNameActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    // 권한 요청 런처 초기화
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // 권한이 허용된 경우 FCM 토큰을 받아옴
                handlePushAlarmPermissionGranted { token ->
                    viewModel.setFcmToken(token)
                    Timber.d("fcm token: $token")
                }
            }
        },
    )

    // 컴포지션이 시작될 때 권한을 요청
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Tiramisu 버전 이상에서 POST_NOTIFICATIONS 권한 요청
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // Tiramisu 버전 이하에서는 바로 FCM 토큰을 받아옴
            handlePushAlarmPermissionGranted { token ->
                viewModel.setFcmToken(token)
            }
        }
    }

    LaunchedEffect(lifecycleOwner) {
        viewModel.loginSideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .onEach { sideEffect ->
                when (sideEffect) {
                    is LoginSideEffect.ShowSnackBar -> context.toast(sideEffect.message)
                    is LoginSideEffect.NavigateToMain -> navigateTo<MainActivity>(context)
                    is LoginSideEffect.NavigateToOnBoarding -> navigateTo<SetNameActivity>(context)
                }
            }.launchIn(lifecycleOwner.lifecycleScope)
    }

    LoginScreen(
        onLoginBtnClick = {
            viewModel.startKaKaoLogin(context)
        },
    )
}

private fun handlePushAlarmPermissionGranted(onTokenReceived: (String) -> Unit) {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val token = task.result
            onTokenReceived(token)
        } else {
            Timber.d(task.exception)
        }
    }
}

@Composable
fun LoginScreen(
    onLoginBtnClick: () -> Unit,
) {
    SetStatusBarColor(color = Green4.toArgb())

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_login),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f))

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
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun SetStatusBarColor(color: Int) {
    val activity = LocalContext.current as? Activity
    SideEffect {
        activity?.window?.let { window ->
            WindowCompat.getInsetsController(
                window,
                window.decorView,
            ).isAppearanceLightStatusBars = false // Set to false if your status bar color is light
            window.statusBarColor = color
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
