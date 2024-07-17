package com.teamkkumul.feature.meetup.meetupdetail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpDetailBinding
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.itemdecorator.MeetUpFriendItemDecoration
import com.teamkkumul.model.MeetUpDetailModel
import com.teamkkumul.model.MeetUpParticipantModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MeetUpDetailFragment :
    BindingFragment<FragmentMeetUpDetailBinding>(R.layout.fragment_meet_up_detail) {
    private val viewModel: MeetUpDetailFriendViewModel by viewModels()

    private var _meetUpDetailAdapter: MeetUpDetailListAdapter? = null
    private val meetUpDetailAdapter get() = requireNotNull(_meetUpDetailAdapter)

    private val promiseId: Int by lazy {
        requireArguments().getInt(PROMISE_ID)
    }

    override fun initView() {
        initMemberRecyclerView()
        viewModel.getMeetUpParticipant(0)
        viewModel.getMeetUpParticipantList(0)
        viewModel.getMeetUpDetail(0)
        initObserveMeetUpDetailState()
        initObserveMeetUpParticipantListState()
        initObserveMeetUpParticipantState()
    }

    private fun initObserveMeetUpDetailState() {
        viewModel.meetupDetailState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Failure -> Timber.tag("meetupdetail").d(uiState.errorMessage)
                is UiState.Success -> successMeetUpDetailState(uiState.data)
                else -> {}
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun successMeetUpDetailState(meetUpDetailModel: MeetUpDetailModel) {
        binding.tvMeetUpDetailLocation.text = meetUpDetailModel.placeName
        binding.tvMeetUpDetailTime.text = meetUpDetailModel.time
        binding.tvMeetUpDetailReadyLevel.text = meetUpDetailModel.dressUpLevel
        binding.tvMeetUpDetailPenalty.text = meetUpDetailModel.penalty
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
        binding.tvMeetUpParticipatePeople.text = meetUpParticipantModel.participantCount.toString()
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
