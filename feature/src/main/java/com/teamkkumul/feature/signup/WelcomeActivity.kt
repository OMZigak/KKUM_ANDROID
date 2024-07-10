package com.teamkkumul.feature.signup

import androidx.activity.viewModels
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.core.ui.util.intent.navigateTo
import com.teamkkumul.feature.MainActivity
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.ActivityWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : BindingActivity<ActivityWelcomeBinding>(R.layout.activity_welcome) {

    private val setNameViewModel: NameViewModel by viewModels()
    override fun initView() {
        setNameViewModel.inputName.observe(this) { inputName ->
            binding.tvWelcome.text = "${inputName}님 반가워요!"
        }
        binding.btnOkay.setOnClickListener {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        navigateTo<MainActivity>(this)
    }
}
