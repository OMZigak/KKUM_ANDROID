package com.teamkkumul.feature.meetup

import com.google.android.material.tabs.TabLayoutMediator
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpContainerBinding
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.KeyStorage.TAB_INDEX

class MeetUpContainerFragment :
    BindingFragment<FragmentMeetUpContainerBinding>(R.layout.fragment_meet_up_container) {
    override fun initView() {
        binding.toolbarMeetUpContainer.toolbarTitle.text = "내모임"
        val promiseId = arguments?.getInt(PROMISE_ID) ?: -1
        initMyPageTabLayout(promiseId)
    }

    private fun initMyPageTabLayout(promiseId: Int) = with(binding) {
        vpMeetUpContainer.adapter =
            MeetUpContainerVpAdapter(this@MeetUpContainerFragment, promiseId)

        val tabTitleArray = arrayOf(
            MEETUP_INFO,
            READY_STATUS,
            LATE_PERSON,
        )

        TabLayoutMediator(tabMeetUpContainer, vpMeetUpContainer) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

        vpMeetUpContainer.setUserInputEnabled(false)

        val tabIndex = arguments?.getInt(TAB_INDEX) ?: 0
        vpMeetUpContainer.setCurrentItem(tabIndex, false)
    }

    companion object {
        private const val MEETUP_INFO = "약속 정보"
        private const val READY_STATUS = "준비 현황"
        private const val LATE_PERSON = "지각 꾸물이"
    }
}
