package com.teamkkumul.feature.meetup.readystatus.readystatus

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.toast
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.core.ui.view.setInVisible
import com.teamkkumul.core.ui.view.setVisible
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentReadyStatusBinding
import com.teamkkumul.feature.meetup.readystatus.readystatus.viewholder.ReadyStatusFriendItemDecoration
import com.teamkkumul.feature.meetupcreate.MeetUpSharedViewModel
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.KeyStorage.READY_STATUS_INFO
import com.teamkkumul.feature.utils.PROGRESS.PROGRESS_NUM_100
import com.teamkkumul.feature.utils.animateProgressBar
import com.teamkkumul.feature.utils.model.BtnState
import com.teamkkumul.feature.utils.time.TimeUtils.isPastDefaultTime
import com.teamkkumul.feature.utils.time.calculateReadyStartTime
import com.teamkkumul.feature.utils.time.formatTimeToKoreanStyle
import com.teamkkumul.feature.utils.time.getCurrentTime
import com.teamkkumul.feature.utils.time.parseMinutesToHoursAndMinutes
import com.teamkkumul.model.home.HomeReadyStatusModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ReadyStatusFragment :
    BindingFragment<FragmentReadyStatusBinding>(R.layout.fragment_ready_status) {
    private val viewModel: ReadyStatusViewModel by viewModels()
    private val sharedViewModel by activityViewModels<MeetUpSharedViewModel>()

    private var _readyStatusAdapter: ReadyStatusAdapter? = null
    private val readyStatusAdapter get() = requireNotNull(_readyStatusAdapter)

    private val promiseId: Int by lazy {
        requireArguments().getInt(PROMISE_ID)
    }

    override fun initView() {
        initReadyStatusBtnClick()
        initObserveBtnState()
        initReadyStatusRecyclerview()
        initObserveReadyStatusState()
        initObserveMembersReadyStatus()
        initObservePopUpVisible()
        initObserveCurrentTimeText()
    }

    private fun initObservePopUpVisible() {
        viewModel.popUpVisible.flowWithLifecycle(viewLifeCycle).onEach {
            binding.groupReadyLatePopUp.setVisible(it)
        }.launchIn(viewLifeCycleScope)
    }

    private fun initObserveCurrentTimeText() {
        viewModel.timeTextState.flowWithLifecycle(viewLifeCycle).onEach {
            binding.tvHomeReadyTime.text = it.readyTime
            binding.tvHomeMovingTime.text = it.movingTime
            binding.tvHomeArriveTime.text = it.completedTime
        }.launchIn(viewLifeCycleScope)
    }

    private fun initObserveMembersReadyStatus() {
        viewModel.getMembersReadyStatus(promiseId)
        viewModel.membersReadyStatus.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> readyStatusAdapter.submitList(it.data)
                is UiState.Failure -> Timber.e(it.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initObserveReadyStatusState() {
        viewModel.getReadyStatus(promiseId)
        viewModel.readyStatusState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> updateReadyStatusUI(it.data)
                is UiState.Failure -> Timber.e(it.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun updateReadyStatusUI(data: HomeReadyStatusModel?) = with(binding) {
        data ?: return
        updateBasicUI(data)
        handleButtonClicks(data)
        initReadyInputBtnClick(data)

        val preparationAvailable = data.preparationTime != null
        updateReadyTimeAlarmVisibility(preparationAvailable)

        if (!preparationAvailable) return
        updateReadyAndMovingTimes(data)
        updateDescriptions(data)
    }

    private fun updateBasicUI(data: HomeReadyStatusModel?) = with(binding) {
        data ?: return
        viewModel.updateReadyTime(data.preparationStartAt ?: "")
        viewModel.updateMovingTime(data.departureAt ?: "")
        viewModel.updateCompletedTime(data.arrivalAt ?: "")
    }

    private fun updateReadyTimeAlarmVisibility(preparationAvailable: Boolean) = with(binding) {
        groupReadyInfoInput.setVisible(preparationAvailable)
        if (preparationAvailable) btnReadyInfoInputEdit.setVisible(!isPastDefaultTime(viewModel.getPromiseTime()))
        tvReadyInfoNext.setVisible(!preparationAvailable)
    }

    private fun updateReadyAndMovingTimes(data: HomeReadyStatusModel) {
        val newReadyTime =
            calculateReadyStartTime(data.promiseTime, data.preparationTime, data.travelTime)
        spannableReadyStartTimeString(formatTimeToKoreanStyle(newReadyTime))

        val newMovingTime = calculateReadyStartTime(data.promiseTime, 0, data.travelTime)
        spannableMovingStartTimeString(formatTimeToKoreanStyle(newMovingTime))
    }

    private fun updateDescriptions(data: HomeReadyStatusModel) {
        val formatReadyTime = parseMinutesToHoursAndMinutes(data.preparationTime)
        binding.tvReadyInfoInputReadyDescription.text = "준비 소요시간: $formatReadyTime"

        val formatMovingTime = parseMinutesToHoursAndMinutes(data.travelTime)
        binding.tvReadyInfoInputMovingDescription.text = "이동 소요시간: $formatMovingTime"
    }

    private fun handleButtonClicks(data: HomeReadyStatusModel) {
        when {
            data.preparationStartAt != null && data.departureAt != null && data.arrivalAt != null -> {
                viewModel.clickCompletedBtn()
            }

            data.departureAt != null && data.preparationStartAt != null -> {
                viewModel.clickMovingStartBtn()
            }

            data.preparationStartAt != null -> {
                viewModel.clickReadyBtn()
            }
        }
    }

    private fun spannableReadyStartTimeString(text: String) {
        val fullText = getString(R.string.ready_status_ready_start, text)
        val spannable = SpannableString(fullText)

        val start = fullText.indexOf(text)
        val end = start + text.length

        spannable.setSpan(
            ForegroundColorSpan(colorOf(R.color.main_color)),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )

        binding.tvReadyInfoInputReadyTime.text = spannable
    }

    private fun spannableMovingStartTimeString(text: String) {
        val fullText = getString(R.string.ready_status_moving_start, text)
        val spannable = SpannableString(fullText)

        val start = fullText.indexOf(text)
        val end = start + text.length

        spannable.setSpan(
            ForegroundColorSpan(colorOf(R.color.main_color)),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )

        binding.tvReadyInfoInputMovingTime.text = spannable
    }

    private fun initReadyInputBtnClick(data: HomeReadyStatusModel) {
        binding.tvReadyInfoNext.setOnClickListener {
            if (isNotParticipantOrLateMeeting()) return@setOnClickListener
            navigateToReadyInfoInputFragment(data)
        }

        binding.btnReadyInfoInputEdit.setOnClickListener {
            if (isNotParticipantOrLateMeeting()) return@setOnClickListener
            navigateToReadyInfoInputFragment(data)
        }
    }

    private fun navigateToReadyInfoInputFragment(data: HomeReadyStatusModel) {
        findNavController().navigate(
            R.id.action_fragment_meet_up_container_to_readyInfoInputFragment,
            bundleOf(
                READY_STATUS_INFO to data.copy(
                    promiseId = promiseId,
                ),
            ),
        )
    }

    private fun isNotParticipantOrLateMeeting(): Boolean {
        return when {
            !sharedViewModel.isParticipant() -> {
                toast(getString(R.string.ready_status_not_participant))
                true
            }

            isPastDefaultTime(viewModel.getPromiseTime()) -> {
                toast(getString(R.string.ready_status_late_meeting))
                true
            }

            else -> false
        }
    }

    private fun initReadyStatusRecyclerview() {
        _readyStatusAdapter = ReadyStatusAdapter()
        binding.rvReadyStatusFriendList.apply {
            adapter = readyStatusAdapter
            addItemDecoration(ReadyStatusFriendItemDecoration(requireContext()))
        }
    }

    private fun initReadyStatusBtnClick() {
        initReadyBtnClick()
        initMovingBtnClick()
        initArriveBtnClick()
    }

    private fun initReadyBtnClick() = with(binding) {
        btnHomeReady.setOnClickListener {
            if (isNotParticipantOrLateMeeting()) return@setOnClickListener
            viewModel.patchReady(promiseId)
        }
        viewModel.readyPatchState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> {
                    viewModel.updateReadyTime(getCurrentTime())
                    viewLifeCycleScope.launch(Dispatchers.Main) {
                        animateProgressBar(pgHomeReady, 0, PROGRESS_NUM_100)
                    }
                }

                is UiState.Failure -> toast(it.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initMovingBtnClick() = with(binding) {
        btnHomeMoving.setOnClickListener {
            viewModel.patchMoving(promiseId)
            viewModel.updateMovingTime(getCurrentTime())
            viewLifeCycleScope.launch(Dispatchers.Main) {
                animateProgressBar(pgHomeMoving, 0, PROGRESS_NUM_100)
            }
        }
    }

    private fun initArriveBtnClick() = with(binding) {
        btnHomeArrive.setOnClickListener {
            viewModel.patchCompleted(promiseId)
            viewModel.updateCompletedTime(getCurrentTime())
            viewLifeCycleScope.launch(Dispatchers.Main) {
                animateProgressBar(pgHomeArrive, 0, PROGRESS_NUM_100)
                delay(300L)
                animateProgressBar(pgHomeArriveEnd, 0, PROGRESS_NUM_100)
            }
        }
    }

    private fun initObserveBtnState() = with(binding) {
        observeBtnState(stateFlow = viewModel.readyBtnState) { state ->
            setUpButton(
                state,
                btnHomeReady,
                ivHomeReadyCircle,
                pgHomeReady,
                null,
                tvHomeReadyHelpText,
            )
        }

        observeBtnState(stateFlow = viewModel.movingStartBtnState) { state ->
            setUpButton(
                state,
                btnHomeMoving,
                ivHomeMovingCircle,
                pgHomeMoving,
                null,
                tvHomeMovingHelpText,
            )
        }

        observeBtnState(stateFlow = viewModel.completedBtnState) { state ->
            setUpButton(
                state,
                btnHomeArrive,
                ivHomeArriveCircle,
                pgHomeArrive,
                pgHomeArriveEnd,
                tvHomeCompletedHelpText,
            )
        }
    }

    private fun observeBtnState(
        stateFlow: StateFlow<BtnState>,
        onStateChanged: (BtnState) -> Unit,
    ) {
        stateFlow.flowWithLifecycle(viewLifeCycle).onEach { state ->
            onStateChanged(state)
        }.launchIn(viewLifeCycleScope)
    }

    private fun setUpButton(
        state: BtnState,
        button: MaterialButton,
        circle: ImageView,
        progressBar: LinearProgressIndicator,
        progressBarEnd: LinearProgressIndicator?,
        helpText: TextView,
    ) {
        button.apply {
            setStrokeColorResource(state.strokeColor)
            setTextColor(colorOf(state.textColor))
            setBackgroundColor(colorOf(state.backGroundColor))
            isEnabled = state.isEnabled
            text = requireContext().getString(state.btnText.label)
        }
        circle.setImageResource(state.circleImage)
        progressBar.progress = state.progress
        if (progressBarEnd != null) {
            progressBarEnd.progress = state.progress
        }
        helpText.setInVisible(state.isHelpTextVisible)
    }

    companion object {
        @JvmStatic
        fun newInstance(promiseId: Int) =
            ReadyStatusFragment().apply {
                arguments = Bundle().apply {
                    putInt(PROMISE_ID, promiseId)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _readyStatusAdapter = null
    }
}
