package com.teamkkumul.feature.meetup

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpContainerBinding
import com.teamkkumul.feature.meetup.meetupdetail.MeetUpDetailFriendViewModel
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.KeyStorage.TAB_INDEX
import com.teamkkumul.model.MeetUpDetailModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class MeetUpContainerFragment :
    BindingFragment<FragmentMeetUpContainerBinding>(R.layout.fragment_meet_up_container) {
    private val viewModel: MeetUpDetailFriendViewModel by activityViewModels<MeetUpDetailFriendViewModel>()

    override fun initView() {
        val promiseId = arguments?.getInt(PROMISE_ID) ?: -1
        viewModel.getMeetUpDetail(promiseId)
        initMyPageTabLayout(promiseId)
        initObservePromiseNameState()
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

    private fun initObservePromiseNameState() {
        viewModel.meetupDetailState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Success -> successPromiseNameState(uiState.data)
                is UiState.Failure -> Timber.tag("promise name").d(uiState.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun successPromiseNameState(meetUpDetailModel: MeetUpDetailModel) {
        binding.toolbarMeetUpContainer.toolbarTitle.text = meetUpDetailModel.promiseName
    }

    companion object {
        private const val MEETUP_INFO = "약속 정보"
        private const val READY_STATUS = "준비 현황"
        private const val LATE_PERSON = "지각 꾸물이"
    }
}
