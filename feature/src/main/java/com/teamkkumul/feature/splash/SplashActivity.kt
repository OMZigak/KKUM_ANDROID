package com.teamkkumul.feature.splash

import android.annotation.SuppressLint
import android.content.Intent
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.core.ui.util.context.statusBarColorOf
import com.teamkkumul.feature.R
import com.teamkkumul.feature.auth.LoginActivity
import com.teamkkumul.feature.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun initView() {
        statusBarColorOf(R.color.main_color)
        activityScope.launch {
            delay(1000)

            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}
