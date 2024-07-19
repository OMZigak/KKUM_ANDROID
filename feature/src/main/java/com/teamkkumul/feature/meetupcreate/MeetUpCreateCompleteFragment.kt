package com.teamkkumul.feature.meetupcreate

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpCreateCompleteBinding
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.animateProgressBar

class MeetUpCreateCompleteFragment :
    BindingFragment<FragmentMeetUpCreateCompleteBinding>(R.layout.fragment_meet_up_create_complete) {
    private val promiseId: Int by lazy {
        requireArguments().getInt(KeyStorage.PROMISE_ID)
    }

    override fun initView() {
        setProgressBar(100)

        binding.btnCreateMeetUpComplete.setOnClickListener {
            findNavController().navigate(
                R.id.action_fragment_meet_up_create_complete_to_fragment_meet_up_detail,
                bundleOf(KeyStorage.PROMISE_ID to promiseId),
            )
        }
    }

    private fun setProgressBar(progress: Int) {
        val progressBar = binding.pbMeetUpCreateComplete
        progressBar.progress = progress
        animateProgressBar(progressBar, 75, progress)
    }
}
