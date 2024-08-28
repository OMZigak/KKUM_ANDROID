package com.teamkkumul.feature.meetup.meetupdetail

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpDetailBinding
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.MeetUpType
import com.teamkkumul.feature.utils.itemdecorator.MeetUpFriendItemDecoration
import com.teamkkumul.feature.utils.time.TimeUtils.calculateDday
import com.teamkkumul.feature.utils.time.TimeUtils.formatTimeToPmAm
import com.teamkkumul.feature.utils.time.TimeUtils.parseDateOnly
import com.teamkkumul.feature.utils.time.TimeUtils.parseDateToMonthDay
import com.teamkkumul.feature.utils.time.TimeUtils.parseTimeOnly
import com.teamkkumul.feature.utils.time.setDdayTextColor
import com.teamkkumul.model.MeetUpDetailModel
import com.teamkkumul.model.MeetUpParticipantModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MeetUpDetailFragment :
    BindingFragment<FragmentMeetUpDetailBinding>(R.layout.fragment_meet_up_detail) {
    private val viewModel: MeetUpDetailFriendViewModel by activityViewModels<MeetUpDetailFriendViewModel>()

    private var _meetUpDetailAdapter: MeetUpDetailListAdapter? = null
    private val meetUpDetailAdapter get() = requireNotNull(_meetUpDetailAdapter)

    private val promiseId: Int by lazy {
        requireArguments().getInt(PROMISE_ID)
    }

    override fun initView() {
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
                is UiState.Success -> {
                    val meetUpDetailModel = uiState.data
                    val bundle = Bundle().apply {
                        putString(KeyStorage.MEET_UP_TYPE, MeetUpType.EDIT.toString())
                        putInt(KeyStorage.PROMISE_ID, promiseId)
                        putString(KeyStorage.MEET_UP_NAME, meetUpDetailModel.promiseName)
                        putString(KeyStorage.MEET_UP_LOCATION, meetUpDetailModel.placeName)
                        putString(
                            KeyStorage.MEET_UP_DATE,
                            meetUpDetailModel.time.parseDateOnly(),
                        )
                        putString(
                            KeyStorage.MEET_UP_TIME,
                            meetUpDetailModel.time.parseTimeOnly(),
                        )
                        putString(KeyStorage.MEET_UP_LOCATION_X, meetUpDetailModel.x.toString())
                        putString(KeyStorage.MEET_UP_LOCATION_Y, meetUpDetailModel.y.toString())
                        putString(KeyStorage.MEET_UP_LEVEL, meetUpDetailModel.dressUpLevel)
                        putString(KeyStorage.MEET_UP_PENALTY, meetUpDetailModel.penalty)
                    }
                    Timber.tag("meet").d(bundle.toString())
                    findNavController().navigate(
                        R.id.action_fragment_meet_up_container_to_meetUpCreateFragment,
                        bundle,
                    )
                }

                is UiState.Failure -> Timber.tag("MeetUpDetailFragment").d(uiState.errorMessage)
                else -> {}
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun successMeetUpDetailState(meetUpDetailModel: MeetUpDetailModel) {
        val dDay = meetUpDetailModel.time.calculateDday()
        val dDayInt: Int = 0
        with(binding) {
            tvMeetUpName.text = meetUpDetailModel.promiseName
            tvMeetUpDetailLocation.text = meetUpDetailModel.placeName
            tvMeetUpDetailTime.text =
                "${meetUpDetailModel.time.parseDateToMonthDay()} ${meetUpDetailModel.time.formatTimeToPmAm()}"
            tvMeetUpDetailReadyLevel.text = meetUpDetailModel.dressUpLevel
            tvMeetUpDetailPenalty.text = meetUpDetailModel.penalty
            tvMeetUpDetailDday.text = dDay
            tvMeetUpDetailDday.setTextColor(colorOf(setDdayTextColor(dDayInt)) ?: R.color.gray3)

            if (dDayInt > 0) { // 잠만 이렇게 toInt 쓰면 안됨 -> D-DAY를 어케 toInt를 하니
                ivMeetUpDday.setImageResource(R.drawable.ic_meet_up_detail_receipt_gray)
                // groupMeetUpDetail.setTextColor(colorOf(R.color.gray4))
            } else {
                ivMeetUpDday.setImageResource(R.drawable.ic_meet_up_detail_receipt)
                // groupMeetUpDetail.setTextColor(colorOf(R.color.main_color))
            }
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
