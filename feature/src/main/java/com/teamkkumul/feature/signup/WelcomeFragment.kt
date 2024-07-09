package com.teamkkumul.feature.signup

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : BindingFragment<FragmentWelcomeBinding>(R.layout.fragment_welcome) {

    private val setNameViewModel: NameViewModel by activityViewModels()
    override fun initView() {
        setNameViewModel.inputName.observe(viewLifecycleOwner) { inputName ->
            binding.tvWelcome.text = "${inputName}님 반가워요!"
        }
        binding.btnOkay.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_welcome_to_fragment_home)
        }
    }
}
