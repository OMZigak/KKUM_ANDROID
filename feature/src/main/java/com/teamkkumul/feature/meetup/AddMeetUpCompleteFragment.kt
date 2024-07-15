package com.teamkkumul.feature.meetup

import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentAddMeetUpCompleteBinding

class AddMeetUpCompleteFragment :
    BindingFragment<FragmentAddMeetUpCompleteBinding>(R.layout.fragment_add_meet_up_complete) {
    override fun initView() {
        binding.btnCreateMeetUpComplete.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_add_meet_up_complete_to_fragment_meet_up_detail)
        }
    }
}
