package com.teamkkumul.feature.meetup.readystatus.complete

import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentReadyInputCompletedBinding

class ReadyInputCompletedFragment :
    BindingFragment<FragmentReadyInputCompletedBinding>(R.layout.fragment_ready_input_completed) {
    override fun initView() {
        binding.btnReadyInfoCompletedNext.setOnClickListener {
            findNavController().navigate(R.id.action_readyInputCompletedFragment_to_fragment_meet_up_container)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_readyInputCompletedFragment_to_fragment_meet_up_container)
                }
            },
        )
    }
}
