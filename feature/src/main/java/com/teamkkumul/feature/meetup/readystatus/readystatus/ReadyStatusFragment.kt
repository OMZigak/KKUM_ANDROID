package com.teamkkumul.feature.meetup.readystatus.readystatus

import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentReadyStatusBinding
import com.teamkkumul.feature.meetup.readystatus.viewholder.ReadyStatusFriendItemDecoration
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.PROGRESS.PROGRESS_NUM_100
import com.teamkkumul.feature.utils.PROGRESS.PROGRESS_TIME
import com.teamkkumul.feature.utils.animateProgressBar
import com.teamkkumul.feature.utils.getCurrentTime
import com.teamkkumul.feature.utils.model.BtnState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

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
    }

    private fun initReadyInputBtnClick() {
        binding.tvReadyInfoNext.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_meet_up_container_to_readyInfoInputFragment)
        }
    }

    private fun initReadyStatusRecyclerview() {
        _readyStatusAdapter = ReadyStatusAdapter().apply {
            submitList(viewModel.mockMembers)
        }
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
                delay(PROGRESS_TIME)
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

    companion object {
        @JvmStatic
        fun newInstance(promiseId: Int) = ReadyStatusFragment().apply {
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
