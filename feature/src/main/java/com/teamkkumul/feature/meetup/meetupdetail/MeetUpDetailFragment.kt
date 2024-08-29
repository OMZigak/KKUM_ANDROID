package com.teamkkumul.feature.meetup.meetupdetail

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.toast
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpDetailBinding
import com.teamkkumul.feature.meetupcreate.MeetUpSharedViewModel
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.MeetUpType
import com.teamkkumul.feature.utils.itemdecorator.MeetUpFriendItemDecoration
import com.teamkkumul.feature.utils.time.TimeUtils.calculateDday
import com.teamkkumul.feature.utils.time.TimeUtils.formatTimeToPmAm
import com.teamkkumul.feature.utils.time.TimeUtils.parseDateOnly
import com.teamkkumul.feature.utils.time.TimeUtils.parseDateToMonthDay
import com.teamkkumul.feature.utils.time.TimeUtils.parseTimeOnly
import com.teamkkumul.feature.utils.time.setDday
import com.teamkkumul.feature.utils.time.setDdayTextColor
import com.teamkkumul.feature.utils.time.setMeetUpDetailImage
import com.teamkkumul.feature.utils.time.setMeetUpDetailTextColor
import com.teamkkumul.feature.utils.time.setMeetUpTitleColor
import com.teamkkumul.model.MeetUpDetailModel
import com.teamkkumul.model.MeetUpParticipantModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MeetUpDetailFragment :
    BindingFragment<FragmentMeetUpDetailBinding>(R.layout.fragment_meet_up_detail) {
    private val viewModel: MeetUpDetailFriendViewModel by viewModels<MeetUpDetailFriendViewModel>()
    private val sharedViewModel: MeetUpSharedViewModel by activityViewModels<MeetUpSharedViewModel>()
    private var _meetUpDetailAdapter: MeetUpDetailListAdapter? = null
    private val meetUpDetailAdapter get() = requireNotNull(_meetUpDetailAdapter)

    private val promiseId: Int by lazy {
        requireArguments().getInt(PROMISE_ID)
    }

    override fun initView() {
        sharedViewModel.updateMeetUpModel(meetupType = MeetUpType.CREATE.name)
        initMemberRecyclerView()
        viewModel.getMeetUpParticipant(promiseId)
        viewModel.getMeetUpParticipantList(promiseId)
        viewModel.getMeetUpDetail(promiseId)
        initObserveMeetUpDetailState()
        initObserveMeetUpParticipantListState()
        initObserveMeetUpParticipantState()

        binding.btnMeetUpDetailEdit.setOnClickListener {
            navigateToEditMeetUp()
        }
    }

    private fun initObserveMeetUpDetailState() {
        viewModel.meetupDetailState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Failure -> Timber.tag("meet up detail").d(uiState.errorMessage)
                is UiState.Success -> successMeetUpDetailState(uiState.data)
                else -> {}
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun navigateToEditMeetUp() {
        viewModel.meetupDetailState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Failure -> toast("수정하기가 불가 합니다")
                is UiState.Success -> {
                    updateSharedViewModel(uiState.data)
                    findNavController().navigate(
                        R.id.action_fragment_meet_up_container_to_meetUpCreateFragment,
                    )
                }

                else -> {}
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun updateSharedViewModel(data: MeetUpDetailModel) {
        sharedViewModel.updateMeetUpModel(
            meetupType = MeetUpType.EDIT.name,
            promiseId = data.promiseId,
            name = data.promiseName,
            placeName = data.placeName,
            time = data.time.parseTimeOnly(),
            date = data.time.parseDateOnly(),
            x = data.x,
            y = data.y,
            penalty = data.penalty,
            dressUpLevel = data.dressUpLevel,
        )
    }

    private fun successMeetUpDetailState(meetUpDetailModel: MeetUpDetailModel) {
        val dDay = meetUpDetailModel.time.calculateDday()
        with(binding) {
            tvMeetUpName.text = meetUpDetailModel.promiseName
            tvMeetUpDetailLocation.text = meetUpDetailModel.placeName
            tvMeetUpDetailTime.text =
                "${meetUpDetailModel.time.parseDateToMonthDay()} ${meetUpDetailModel.time.formatTimeToPmAm()}"
            tvMeetUpDetailReadyLevel.text = meetUpDetailModel.dressUpLevel
            tvMeetUpDetailPenalty.text = meetUpDetailModel.penalty
            tvMeetUpDetailDday.text = setDday(dDay)
            tvMeetUpDetailDday.setTextColor(colorOf(setDdayTextColor(dDay)))

            ivMeetUpDday.load(setMeetUpDetailImage(dDay))
            tvMeetUpName.setTextColor(colorOf(setMeetUpTitleColor(dDay)))
            tvMeetUpParticipatePeople.setTextColor(colorOf(setMeetUpDetailTextColor(dDay)))
            tvMeetUpDetailInformationLocation.setTextColor(colorOf(setMeetUpDetailTextColor(dDay)))
            tvMeetUpDetailInformationTime.setTextColor(colorOf(setMeetUpDetailTextColor(dDay)))
            tvMeetUpDetailInformationReadyLevel.setTextColor(colorOf(setMeetUpDetailTextColor(dDay)))
            tvMeetUpDetailInformationPenalty.setTextColor(colorOf(setMeetUpDetailTextColor(dDay)))
        }
    }

    private fun initObserveMeetUpParticipantState() {
        viewModel.meetUpParticipantState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Failure -> Timber.tag("meetupdetail").d(uiState.errorMessage)
                is UiState.Success -> successParticipantState(uiState.data)
                else -> {}
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun successParticipantState(meetUpParticipantModel: MeetUpParticipantModel) {
        binding.tvMeetUpParticipatePeopleCount.text =
            meetUpParticipantModel.participantCount.toString()
    }

    private fun initObserveMeetUpParticipantListState() {
        viewModel.meetUpParticipantListState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Failure -> Timber.tag("meet up participant").d(uiState.errorMessage)
                is UiState.Success -> {
                    meetUpDetailAdapter.submitList(uiState.data)
                }

                else -> {}
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initMemberRecyclerView() {
        _meetUpDetailAdapter = MeetUpDetailListAdapter()
        binding.rvMeetUpFriendList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = meetUpDetailAdapter
            addItemDecoration(MeetUpFriendItemDecoration(requireContext()))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(promiseId: Int) = MeetUpDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(PROMISE_ID, promiseId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _meetUpDetailAdapter = null
    }
}
