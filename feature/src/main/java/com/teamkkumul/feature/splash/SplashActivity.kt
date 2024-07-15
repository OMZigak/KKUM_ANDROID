package com.teamkkumul.feature.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.core.ui.util.context.statusBarColorOf
import com.teamkkumul.feature.R
import com.teamkkumul.feature.auth.LoginActivity
import com.teamkkumul.feature.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        statusBarColorOf(R.color.main_color)
        lifecycleScope.launch {
            delay(1000)

            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
