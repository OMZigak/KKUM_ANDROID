package com.teamkkumul.feature.home

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.toast
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentHomeBinding
import com.teamkkumul.feature.home.model.BtnState
import com.teamkkumul.feature.mygroup.MyGroupMeetUpItemDecoration
import com.teamkkumul.feature.utils.AnimateProgressBarCommon
import com.teamkkumul.feature.utils.getCurrentTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>()

    private var _homeMeetUpAdapter: HomeMeetUpAdapter? = null
    private val homeMeetUpAdapter get() = requireNotNull(_homeMeetUpAdapter)

    override fun initView() {
        initHomeBtnClick()
        initObserveBtnState()
        initMeetingNextBtnClick()
    }

    private fun initHomeBtnClick() {
        initReadyBtnClick()
        initMovingBtnClick()
        initArriveBtnClick()
        initHomeMeetUpRecyclerView()
        initObserveHomePromiseState()
    }

    private fun initObserveHomePromiseState() {
        viewModel.homePromiseState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> {
                    showPromiseRecyclerView()
//                    homeMeetUpAdapter.submitList(it.data)
                }

                is UiState.Empty -> showEmptyView()

                is UiState.Failure -> toast(it.errorMessage)
                is UiState.Loading -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun showPromiseRecyclerView() {
        binding.rvMyGroupMeetUp.visibility = View.VISIBLE
        binding.viewHomePromiseEmpty.visibility = View.GONE
    }

    private fun showEmptyView() {
        binding.rvMyGroupMeetUp.visibility = View.GONE
        binding.viewHomePromiseEmpty.visibility = View.VISIBLE
    }

    private fun animateProgressBar(progressBar: ProgressBar, progress: Int) {
        val anim = AnimateProgressBarCommon(progressBar, 0f, progress.toFloat())
        anim.duration = 500
        progressBar.startAnimation(anim)
    }

    private fun initReadyBtnClick() = with(binding) {
        btnHomeReady.setOnClickListener {
            viewModel.clickReadyBtn()
            tvHomeReadyTime.text = getCurrentTime()
            viewLifeCycleScope.launch {
                animateProgressBar(pgHomeReady, progressNum)
            }
        }
    }

    private fun initMovingBtnClick() = with(binding) {
        btnHomeMoving.setOnClickListener {
            viewModel.clickMovingStartBtn()
            tvHomeMovingTime.text = getCurrentTime()
            viewLifeCycleScope.launch {
                animateProgressBar(pgHomeMoving, progressNum)
            }
        }
    }

    private fun initArriveBtnClick() = with(binding) {
        btnHomeArrive.setOnClickListener {
            viewModel.clickCompletedBtn()
            tvvHomeArriveTime.text = getCurrentTime()

            viewLifeCycleScope.launch {
                animateProgressBar(pgHomeArrive, progressNum)
                delay(300L)
                animateProgressBar(pgHomeArriveEnd, progressNum)
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
            onMeetUpDetailBtnClicked = {
                findNavController().navigate(R.id.exampleComposeFragment) // 임시로 이동하는 페이지
            },
        ).apply {
            submitList(viewModel.mockMembers)
        }
        binding.rvMyGroupMeetUp.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = homeMeetUpAdapter
            addItemDecoration(MyGroupMeetUpItemDecoration(requireContext()))
        }
    }

    private fun initMeetingNextBtnClick() {
        binding.ivHomeMeetingNext.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_home_to_meetUpContainerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeMeetUpAdapter = null
    }

    companion object {
        private const val progressNum = 100
    }
}
