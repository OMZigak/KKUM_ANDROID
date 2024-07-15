package com.teamkkumul.feature.meetupcreate

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpCreateCompleteBinding
import com.teamkkumul.feature.utils.animateProgressBar

class MeetUpCreateCompleteFragment :
    BindingFragment<FragmentMeetUpCreateCompleteBinding>(R.layout.fragment_meet_up_create_complete) {

    private val viewModel: MeetUpCreateViewModel by activityViewModels<MeetUpCreateViewModel>()
    override fun initView() {
        viewModel.setProgressBar(100)
        observeProgress()

        binding.btnCreateMeetUpComplete.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_meet_up_create_complete_to_fragment_meet_up_detail)
        }
    }

    private fun observeProgress() {
        val progressBar = binding.pbMeetUpCreateComplete
        viewModel.progressLiveData.observe(viewLifecycleOwner) { progress ->
            progressBar.progress = progress
            animateProgressBar(progressBar, 75, progress)
        }
    }
}
