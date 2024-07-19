package com.teamkkumul.feature.meetupcreate

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpCreateCompleteBinding
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.animateProgressBar
import timber.log.Timber

class MeetUpCreateCompleteFragment :
    BindingFragment<FragmentMeetUpCreateCompleteBinding>(R.layout.fragment_meet_up_create_complete) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val meetingId = arguments?.getInt(KeyStorage.MEETING_ID)
        val meetUpLevel = arguments?.getString(KeyStorage.MEET_UP_LEVEL)
        val penalty = arguments?.getString(KeyStorage.PENALTY)

        Timber.d("Meeting ID!!!!: $meetingId")
        Timber.d("Meet Up Level!!: $meetUpLevel")
        Timber.d("Penalty: $penalty")
    }

    override fun initView() {
        setProgressBar(100)

        binding.btnCreateMeetUpComplete.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_meet_up_create_complete_to_fragment_meet_up_detail)
        }
    }

    private fun setProgressBar(progress: Int) {
        val progressBar = binding.pbMeetUpCreateComplete
        progressBar.progress = progress
        animateProgressBar(progressBar, 75, progress)
    }
}
