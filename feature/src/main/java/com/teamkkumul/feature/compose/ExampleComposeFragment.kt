package com.teamkkumul.feature.compose

import com.teamkkumul.core.designsystem.theme.KkumulAndroidTheme
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentExampleComposeBinding

class ExampleComposeFragment :
    BindingFragment<FragmentExampleComposeBinding>(R.layout.fragment_example_compose) {
    override fun initView() {
        binding.composeExample.setContent {
            KkumulAndroidTheme {
                ExampleRoute()
            }
        }
    }
}
