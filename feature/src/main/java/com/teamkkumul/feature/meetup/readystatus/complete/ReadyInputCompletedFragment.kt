package com.teamkkumul.feature.meetup.readystatus.complete

import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentReadyInputCompletedBinding
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.KeyStorage.TAB_INDEX

class ReadyInputCompletedFragment :
    BindingFragment<FragmentReadyInputCompletedBinding>(R.layout.fragment_ready_input_completed) {
    private val promiseId: Int by lazy {
        requireArguments().getInt(PROMISE_ID)
    }

    override fun initView() {
        binding.btnReadyInfoCompletedNext.setOnClickListener {
            findNavController().navigate(
                R.id.action_readyInputCompletedFragment_to_fragment_meet_up_container,
                bundleOf(
                    PROMISE_ID to promiseId,
                    TAB_INDEX to 1,
                ),
            )
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(
                        R.id.action_readyInputCompletedFragment_to_fragment_meet_up_container,
                        bundleOf(
                            PROMISE_ID to promiseId,
                            TAB_INDEX to 1,
                        ),
                    )
                }
            },
        )
    }
}
