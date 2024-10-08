package com.teamkkumul.feature.meetup

import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.core.ui.view.setVisible
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpContainerBinding
import com.teamkkumul.feature.meetupcreate.MeetUpSharedViewModel
import com.teamkkumul.feature.utils.KeyStorage.MEET_UP_NAME
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.KeyStorage.TAB_INDEX
import com.teamkkumul.model.MeetUpDetailModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class MeetUpContainerFragment :
    BindingFragment<FragmentMeetUpContainerBinding>(R.layout.fragment_meet_up_container) {
    private val sharedViewModel by activityViewModels<MeetUpSharedViewModel>()

    private val currentId: Int by lazy { arguments?.getInt(PROMISE_ID, -1) ?: -1 }
    private val promiseId: Int by lazy { arguments?.getInt(PROMISE_ID) ?: -1 }

    override fun initView() {
        sharedViewModel.getMeetUpDetail(promiseId)
        initMyPageTabLayout(promiseId)
        initObservePromiseNameState()
        navigationClickListener()
        binding.toolbarMeetUpContainer.toolbarMyPageLine.setVisible(false)
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
        sharedViewModel.meetupDetailState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Success -> successMeetupDetailAppbarState(uiState.data)
                is UiState.Failure -> Timber.e(uiState.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun successMeetupDetailAppbarState(meetUpDetailModel: MeetUpDetailModel) {
        with(binding) {
            toolbarMeetUpContainer.toolbarTitle.text = meetUpDetailModel.promiseName
            toolbarMeetUpContainer.ivBtnMore.setVisible(meetUpDetailModel.isParticipant == true)
        }
    }

    private fun navigationClickListener() {
        binding.toolbarMeetUpContainer.ivBtnMore.setOnClickListener {
            val meetUpName = binding.toolbarMeetUpContainer.toolbarTitle.text.toString()
            if (meetUpName.isNotBlank()) {
                findNavController().navigate(
                    R.id.action_meetUpContainerFragment_to_exitMeetUpBottomSheetFragment,
                    bundleOf(PROMISE_ID to currentId, MEET_UP_NAME to meetUpName),
                )
            }
        }
    }

    companion object {
        private const val MEETUP_INFO = "약속 정보"
        private const val READY_STATUS = "준비 현황"
        private const val LATE_PERSON = "지각 꾸물이"
    }
}
