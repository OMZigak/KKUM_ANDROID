package com.teamkkumul.feature.home.meetupdetail

import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpContainerBinding

class MeetUpContainerFragment :
    BindingFragment<FragmentMeetUpContainerBinding>(R.layout.fragment_meet_up_container) {
    override fun initView() {
        binding.toolbarMeetUpContainer.toolbarTitle.text = "내모임"
        initMyPageTabLayout()
    }

    private fun initMyPageTabLayout() = with(binding) {
        vpMeetUpContainer.adapter = MeetUpDetailVpAdapter(this@MeetUpContainerFragment)
        vpMeetUpContainer.setUserInputEnabled(false)
    }
}
