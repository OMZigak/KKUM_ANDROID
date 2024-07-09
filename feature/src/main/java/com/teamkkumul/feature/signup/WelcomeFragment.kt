package com.teamkkumul.feature.signup

import androidx.fragment.app.activityViewModels
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : BindingFragment<FragmentWelcomeBinding>(R.layout.fragment_welcome) {

    private val setNameViewModel: SetNameViewModel by activityViewModels()
    override fun initView() {
        setNameViewModel.inputName.observe(viewLifecycleOwner) { inputName ->
            binding.tvWelcome.text = "${inputName}님 반가워요!"
        }
    }
}
