package com.teamkkumul.feature.home

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentHomeBinding
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.PROGRESS.PROGRESS_NUM_100
import com.teamkkumul.feature.utils.animateProgressBar
import com.teamkkumul.feature.utils.getCurrentTime
import com.teamkkumul.feature.utils.itemdecorator.MeetUpFriendItemDecoration
import com.teamkkumul.feature.utils.model.BtnState
import com.teamkkumul.model.home.HomeTodayMeetingModel
import com.teamkkumul.model.home.UserModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>()

    private var _homeMeetUpAdapter: HomeMeetUpAdapter? = null
    private val homeMeetUpAdapter get() = requireNotNull(_homeMeetUpAdapter)

    override fun initView() {
        initGetHomeApi()
        initObserveTodayMeetingState()
        initObsereveHomeTopbannerState()
        initHomeBtnClick()
        initObserveBtnState()
        initHomeMeetUpRecyclerView()
        initObserveHomePromiseState()
    }

    private fun initGetHomeApi() {
        viewModel.getUserInfo()
        viewModel.getTodayMeeting()
        viewModel.getUpComingMeeting()
    }

    private fun initObserveTodayMeetingState() {
        viewModel.todayMeetingState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> {
                    updateMeetingVisibility(true)
                    updateTodayMeetingUI(it.data)
                }

                is UiState.Empty -> updateMeetingVisibility(false)

                is UiState.Failure -> Timber.tag("home").d(it.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun updateTodayMeetingUI(data: HomeTodayMeetingModel?) = with(binding) {
        if (data == null) return
        tvHomeGroupText.text = data.name.toString()
        tvHomeMeetingTitle.text = data.meetingName.toString()
        tvHomeMeetingWhere.text = data.placeName.toString()
        tvHomeMeetingTime.text = data.time
        initMeetingNextBtnClick(data.promiseId)
    }

    private fun updateMeetingVisibility(isVisible: Boolean) {
        binding.groupHomeMeeting.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.groupHomeMeetingEmpty.visibility = if (isVisible) View.GONE else View.VISIBLE
        binding.ivHomeMeetingNext.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun initObsereveHomeTopbannerState() {
        viewModel.homeState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> updateHomeTopBannerUI(it.data)
                is UiState.Failure -> Timber.tag("home").d(it.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun updateHomeTopBannerUI(data: UserModel) = with(binding) {
        tvHomeNickname.text = getString(R.string.home_nickname_text, data.name.toString())
        tvHomeMeetingCount.text =
            getString(R.string.home_meeting_count_text, data.promiseCount)
        tvHomeLateCount.text = getString(R.string.home_late_count_text, data.tardyCount)
        updateLevelImage(data.level)
    }

    private fun updateLevelImage(level: Int) = with(binding) {
        when (level) {
            1 -> {
                ivHomeLevel.setImageResource(R.drawable.ic_home_lv_1)
                spannableLevelString(level, getString(R.string.home_lv1))
            }

            2 -> {
                ivHomeLevel.setImageResource(R.drawable.ic_home_lv_2)
                spannableLevelString(level, getString(R.string.home_lv2))
            }

            3 -> {
                ivHomeLevel.setImageResource(R.drawable.ic_home_lv_3)
                spannableLevelString(level, getString(R.string.home_lv3))
            }

            4 -> {
                ivHomeLevel.setImageResource(R.drawable.ic_home_lv_4)
                spannableLevelString(level, getString(R.string.home_lv4))
            }
        }
    }

    private fun spannableLevelString(level: Int, text: String) {
        val fullText = getString(R.string.home_level_text, level, text)
        val spannable = SpannableString(fullText)

        spannable.setSpan(
            ForegroundColorSpan(colorOf(R.color.main_color)),
            0,
            4,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
        binding.tvHomeLevel.text = spannable
    }

    private fun initHomeBtnClick() {
        initReadyBtnClick()
        initMovingBtnClick()
        initArriveBtnClick()
    }

    private fun initObserveHomePromiseState() {
        viewModel.upComingMeetingState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> {
                    updateUpComingMeetingVisibility(true)
                    homeMeetUpAdapter.submitList(it.data)
                }

                is UiState.Empty -> updateUpComingMeetingVisibility(false)

                is UiState.Failure -> Timber.tag("home").d(it.errorMessage)
                is UiState.Loading -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun updateUpComingMeetingVisibility(isVisible: Boolean) {
        binding.rvMyGroupMeetUp.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.viewHomePromiseEmpty.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    private fun initReadyBtnClick() = with(binding) {
        btnHomeReady.setOnClickListener {
            viewModel.clickReadyBtn()
            tvHomeReadyTime.text = getCurrentTime()
            viewLifeCycleScope.launch {
                animateProgressBar(pgHomeReady, 0, PROGRESS_NUM_100)
            }
        }
    }

    private fun initMovingBtnClick() = with(binding) {
        btnHomeMoving.setOnClickListener {
            viewModel.clickMovingStartBtn()
            tvHomeMovingTime.text = getCurrentTime()
            viewLifeCycleScope.launch {
                animateProgressBar(pgHomeMoving, 0, PROGRESS_NUM_100)
            }
        }
    }

    private fun initArriveBtnClick() = with(binding) {
        btnHomeArrive.setOnClickListener {
            viewModel.clickCompletedBtn()
            tvvHomeArriveTime.text = getCurrentTime()

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
        )
        observeBtnState(
            viewModel.movingStartBtnState,
            binding.btnHomeMoving,
            ivHomeMovingCircle,
        )
        observeBtnState(
            viewModel.completedBtnState,
            binding.btnHomeArrive,
            ivHomeArriveCircle,
        )
    }

    private fun observeBtnState(
        stateFlow: StateFlow<BtnState>,
        button: MaterialButton,
        circle: ImageView,
    ) {
        stateFlow.flowWithLifecycle(viewLifeCycle).onEach { state ->
            setUpButton(state, button, circle)
        }.launchIn(viewLifeCycleScope)
    }

    private fun setUpButton(
        state: BtnState,
        button: MaterialButton,
        circle: ImageView,
    ) {
        button.apply {
            setStrokeColorResource(state.strokeColor)
            setTextColor(colorOf(state.textColor))
            setBackgroundColor(colorOf(state.backGroundColor))
            isEnabled = state.isEnabled
        }
        circle.setImageResource(state.circleImage)
    }

    private fun initHomeMeetUpRecyclerView() {
        _homeMeetUpAdapter = HomeMeetUpAdapter(
            onItemClicked = { promiseId ->
                findNavController().navigate(
                    R.id.action_fragment_home_to_meetUpContainerFragment,
                    bundleOf(PROMISE_ID to promiseId),
                )
            },
        )
        binding.rvMyGroupMeetUp.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = homeMeetUpAdapter
            addItemDecoration(MeetUpFriendItemDecoration(requireContext()))
        }
    }

    private fun initMeetingNextBtnClick(promiseId: Int) {
        binding.ivHomeMeetingNext.setOnClickListener {
            findNavController().navigate(
                R.id.action_fragment_home_to_meetUpContainerFragment,
                bundleOf(PROMISE_ID to promiseId),
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeMeetUpAdapter = null
    }
}
