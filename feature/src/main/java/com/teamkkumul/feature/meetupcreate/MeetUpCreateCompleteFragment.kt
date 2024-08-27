package com.teamkkumul.feature.meetupcreate

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.view.setVisible
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpCreateCompleteBinding
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.MeetUpType
import com.teamkkumul.feature.utils.animateProgressBar
import timber.log.Timber

class MeetUpCreateCompleteFragment :
    BindingFragment<FragmentMeetUpCreateCompleteBinding>(R.layout.fragment_meet_up_create_complete) {
    private val promiseId: Int by lazy {
        requireArguments().getInt(KeyStorage.PROMISE_ID)
    }

    override fun initView() {
        setProgressBar(100)
        val meetUpType =
            arguments?.getString(KeyStorage.MEET_UP_TYPE) ?: MeetUpType.CREATE.toString()
        Timber.tag("checkk").d(meetUpType)

        if (meetUpType == MeetUpType.EDIT.toString()) {
            binding.tbMeetUpCreate.toolbarTitle.text = getString(R.string.meet_up_edit)
            binding.tvAddMeetUpComplete.setVisible(false)
            binding.btnCreateMeetUpComplete.text = getString(R.string.ready_input_completed)
        } else {
            binding.tvEditMeetUpComplete.setVisible(false)
            binding.tvAddMeetUpComplete.setVisible(true)
        }
        binding.btnCreateMeetUpComplete.setOnClickListener {
            findNavController().navigate(
                R.id.action_fragment_meet_up_create_complete_to_fragment_meet_up_container,
                bundleOf(KeyStorage.PROMISE_ID to promiseId),
            )
        }
        binding.tbMeetUpCreate.toolbarMyPageLine.visibility = View.GONE
    }

    private fun setProgressBar(progress: Int) {
        val progressBar = binding.pbMeetUpCreateComplete
        progressBar.progress = progress
        animateProgressBar(progressBar, 75, progress)
    }
}
