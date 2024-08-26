package com.teamkkumul.feature.meetup.readystatus.readystatus

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.core.ui.view.setVisible
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentReadyStatusBinding
import com.teamkkumul.feature.meetup.readystatus.readystatus.viewholder.ReadyStatusFriendItemDecoration
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.PROGRESS.PROGRESS_NUM_100
import com.teamkkumul.feature.utils.animateProgressBar
import com.teamkkumul.feature.utils.model.BtnState
import com.teamkkumul.feature.utils.time.calculateReadyStartTime
import com.teamkkumul.feature.utils.time.getCurrentTime
import com.teamkkumul.feature.utils.time.parseMinutesToHoursAndMinutes
import com.teamkkumul.model.home.HomeReadyStatusModel
import dagger.hilt.android.AndroidEntryPoint
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

    private var _readyStatusAdapter: ReadyStatusAdapter? = null
    private val readyStatusAdapter get() = requireNotNull(_readyStatusAdapter)

    private val promiseId: Int by lazy {
        requireArguments().getInt(PROMISE_ID)
    }

    override fun initView() {
        initReadyStatusBtnClick()
        initObserveBtnState()
        initReadyStatusRecyclerview()
        initReadyInputBtnClick()
        initObserveReadyStatusState()
        initObserveMembersReadyStatus()
        initObservePopUpVisible()
    }

    private fun initObservePopUpVisible() {
        viewModel.popUpVisible.flowWithLifecycle(viewLifeCycle).onEach {
            binding.groupReadyLatePopUp.setVisible(it)
        }.launchIn(viewLifeCycleScope)
    }

    private fun initObserveMembersReadyStatus() {
        viewModel.getMembersReadyStatus(promiseId)
        viewModel.membersReadyStatus.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> readyStatusAdapter.submitList(it.data)
                is UiState.Failure -> Timber.tag("home").d(it.errorMessage)
                is UiState.Empty -> Timber.tag("home").d("empty")
                is UiState.Loading -> Timber.tag("home").d("loading")
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initObserveReadyStatusState() {
        viewModel.getReadyStatus(promiseId)
        viewModel.readyStatusState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> updateReadyStatusUI(it.data)
                is UiState.Failure -> Timber.tag("home").d(it.errorMessage)
                is UiState.Empty -> Timber.tag("home").d("empty")
                is UiState.Loading -> Timber.tag("home").d("loading")
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun updateReadyStatusUI(data: HomeReadyStatusModel?) = with(binding) {
        data ?: return
        updateBasicUI(data)
        handleButtonClicks(data)

        val preparationAvailable = data.preparationTime != null
        updateReadyStatusInfoVisibility(preparationAvailable)

        if (!preparationAvailable) return
        updateReadyAndMovingTimes(data)
        updateDescriptions(data)
    }

    private fun updateBasicUI(data: HomeReadyStatusModel?) = with(binding) {
        data ?: return
        tvHomeReadyTime.text = data.preparationStartAt
        tvHomeMovingTime.text = data.departureAt
        tvHomeArriveTime.text = data.arrivalAt
        viewModel.setPopUpVisible(data.preparationTime == null)
    }

    private fun updateReadyStatusInfoVisibility(preparationAvailable: Boolean) = with(binding) {
        groupReadyInfoInput.setVisible(preparationAvailable)
        tvReadyInfoNext.setVisible(!preparationAvailable)
    }

    private fun updateReadyAndMovingTimes(data: HomeReadyStatusModel) {
        val newReadyTime =
            calculateReadyStartTime(data.promiseTime, data.preparationTime, data.travelTime)
        spannableReadyStartTimeString(newReadyTime)

        val newMovingTime = calculateReadyStartTime(data.promiseTime, 0, data.travelTime)
        spannableMovingStartTimeString(newMovingTime)
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

    private fun initReadyInputBtnClick() {
        binding.tvReadyInfoNext.setOnClickListener {
            findNavController().navigate(
                R.id.action_fragment_meet_up_container_to_readyInfoInputFragment,
                bundleOf(PROMISE_ID to promiseId),
            )
        }

        binding.btnReadyInfoInputEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_fragment_meet_up_container_to_readyInfoInputFragment,
                bundleOf(PROMISE_ID to promiseId),
            )
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
            viewModel.patchReady(promiseId)
            tvHomeReadyTime.text = getCurrentTime()
            viewLifeCycleScope.launch {
                animateProgressBar(pgHomeReady, 0, PROGRESS_NUM_100)
            }
        }
    }

    private fun initMovingBtnClick() = with(binding) {
        btnHomeMoving.setOnClickListener {
            viewModel.patchMoving(promiseId)
            tvHomeMovingTime.text = getCurrentTime()
            viewLifeCycleScope.launch {
                animateProgressBar(pgHomeMoving, 0, PROGRESS_NUM_100)
            }
        }
    }

    private fun initArriveBtnClick() = with(binding) {
        btnHomeArrive.setOnClickListener {
            viewModel.patchCompleted(promiseId)
            tvHomeArriveTime.text = getCurrentTime()
            viewLifeCycleScope.launch {
                animateProgressBar(pgHomeArrive, 0, PROGRESS_NUM_100)
                delay(300L)
                animateProgressBar(pgHomeArriveEnd, 0, PROGRESS_NUM_100)
            }
        }
    }

    private fun initObserveBtnState() = with(binding) {
        observeBtnState(
            viewModel.readyBtnState,
            btnHomeReady,
            ivHomeReadyCircle,
            pgHomeReady,
            null,
        )
        observeBtnState(
            viewModel.movingStartBtnState,
            binding.btnHomeMoving,
            ivHomeMovingCircle,
            pgHomeMoving,
            null,
        )
        observeBtnState(
            viewModel.completedBtnState,
            binding.btnHomeArrive,
            ivHomeArriveCircle,
            pgHomeArrive,
            pgHomeArriveEnd,
        )
    }

    private fun observeBtnState(
        stateFlow: StateFlow<BtnState>,
        button: MaterialButton,
        circle: ImageView,
        progressBar: LinearProgressIndicator,
        progressBarEnd: LinearProgressIndicator?,
    ) {
        stateFlow.flowWithLifecycle(viewLifeCycle).onEach { state ->
            setUpButton(state, button, circle, progressBar, progressBarEnd)
        }.launchIn(viewLifeCycleScope)
    }

    private fun setUpButton(
        state: BtnState,
        button: MaterialButton,
        circle: ImageView,
        progressBar: LinearProgressIndicator,
        progressBarEnd: LinearProgressIndicator?,
    ) {
        button.apply {
            setStrokeColorResource(state.strokeColor)
            setTextColor(colorOf(state.textColor))
            setBackgroundColor(colorOf(state.backGroundColor))
            isEnabled = state.isEnabled
        }
        circle.setImageResource(state.circleImage)
        progressBar.progress = state.progress
        if (progressBarEnd != null) {
            progressBarEnd.progress = state.progress
        }
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
