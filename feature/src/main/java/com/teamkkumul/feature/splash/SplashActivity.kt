package com.teamkkumul.feature.splash

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.core.ui.util.context.statusBarColorOf
import com.teamkkumul.core.ui.util.intent.navigateTo
import com.teamkkumul.feature.MainActivity
import com.teamkkumul.feature.R
import com.teamkkumul.feature.auth.LoginActivity
import com.teamkkumul.feature.databinding.ActivitySplashBinding
import com.teamkkumul.feature.splash.model.AutoLoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel by viewModels<SplashViewModel>()
    override fun initView() {
        statusBarColorOf(R.color.main_color)
        lifecycleScope.launch {
            delay(1000)
            observeAutoLoginState()
        }
    }

    private fun observeAutoLoginState() {
        viewModel.loginState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is AutoLoginState.NavigateToLogin -> navigateTo<LoginActivity>(this)

                is AutoLoginState.NavigateToMain -> navigateTo<MainActivity>(this)

                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }
}
