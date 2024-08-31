package com.teamkkumul.feature.meetupcreate

import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.view.setVisible
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpCreateCompleteBinding
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.animateProgressBar

class MeetUpCreateCompleteFragment :
    BindingFragment<FragmentMeetUpCreateCompleteBinding>(R.layout.fragment_meet_up_create_complete) {
    private val sharedViewModel by activityViewModels<MeetUpSharedViewModel>()

    override fun initView() {
        setProgressBar(100)
        checkMeetUpType()
        initBtnCompletedClickListener()
    }

    private fun checkMeetUpType() {
        if (sharedViewModel.isEditMode()) {
            binding.tbMeetUpCreate.title = getString(R.string.edit_meet_up_title)
            binding.tvAddMeetUpComplete.text = "약속이 수정되었어요!"
            binding.tvAddMeetUpCompleteDescription.text = "해당 약속은 모임 내에서 확인 가능해요"
            binding.btnCreateMeetUpComplete.text = getString(R.string.ready_input_completed)
        } else binding.tbMeetUpCreate.title = getString(R.string.create_meet_up_title)
    }

    private fun initBtnCompletedClickListener() {
        binding.btnCreateMeetUpComplete.setOnClickListener {
            findNavController().navigate(
                R.id.action_fragment_meet_up_create_complete_to_fragment_meet_up_container,
                bundleOf(KeyStorage.PROMISE_ID to sharedViewModel.getPromiseId()),
            )
        }
    }

    private fun setProgressBar(progress: Int) {
        val progressBar = binding.pbMeetUpCreateComplete
        progressBar.progress = progress
        animateProgressBar(progressBar, 75, progress)
        binding.tbMeetUpCreate.toolbarMyPageLine.setVisible(false)
    }
}
