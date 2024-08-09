package com.teamkkumul.feature.meetup.lateperson

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.pxToDp
import com.teamkkumul.core.ui.util.fragment.toast
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.core.ui.view.setVisible
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentLatePersonBinding
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.itemdecorator.GridSpacingItemDecoration
import com.teamkkumul.model.LatePersonModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LatePersonFragment :
    BindingFragment<FragmentLatePersonBinding>(R.layout.fragment_late_person) {

    private val latePersonViewModel: LatePersonViewModel by viewModels()

    private var _latePersonAdapter: LatePersonAdapter? = null
    private val latePersonAdapter get() = requireNotNull(_latePersonAdapter)
    private val promiseId: Int by lazy {
        requireArguments().getInt(KeyStorage.PROMISE_ID)
    }

    override fun initView() {
        initRecyclerView()
        initObserveLatePersonState()
        initObservePatchMeetUpState()
        latePersonViewModel.getLateComersList(promiseId)
        setupMeetUpCompleteBtn()
    }

    private fun initRecyclerView() {
        _latePersonAdapter = LatePersonAdapter()
        binding.rvLatePerson.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = latePersonAdapter
            addItemDecoration(GridSpacingItemDecoration(3, requireContext().pxToDp(8)))
        }
    }

    private fun initObserveLatePersonState() {
        latePersonViewModel.latePersonState.flowWithLifecycle(viewLifeCycle)
            .onEach { latePersonState ->
                when (latePersonState) {
                    is UiState.Success -> handleSuccessState(latePersonState.data)
                    is UiState.Failure -> {
                        showFailureState()
                        updateButtonState(false)
                    }

                    else -> Unit
                }
            }.launchIn(viewLifeCycleScope)
    }

    private fun handleSuccessState(data: LatePersonModel) {
        initPenaltyState(data)
        updateButtonState(data.isPastDue)
        if (!data.isPastDue) {
            showFailureState()
            return
        }
        if (data.lateComers.isEmpty()) {
            showEmptyState()
        } else {
            showLateComers(data.lateComers)
        }
    }

    private fun showFailureState() {
        with(binding) {
            rvLatePerson.setVisible(false)
            viewLatePersonEmpty.setVisible(false)
            viewWaitingEmpty.setVisible(true)
        }
    }

    private fun showEmptyState() {
        with(binding) {
            rvLatePerson.setVisible(false)
            viewLatePersonEmpty.setVisible(true)
            groupPenaltyLatePerson.setVisible(false)
        }
    }

    private fun showLateComers(lateComers: List<LatePersonModel.LateComers>) {
        with(binding) {
            rvLatePerson.setVisible(true)
            viewLatePersonEmpty.setVisible(false)
            viewWaitingEmpty.setVisible(false)
        }
        latePersonAdapter.submitList(lateComers)
    }

    private fun initObservePatchMeetUpState() {
        latePersonViewModel.patchMeetUpState.flowWithLifecycle(viewLifeCycle)
            .onEach { patchMeetUpState ->
                when (patchMeetUpState) {
                    is UiState.Success -> {
                        toast("약속 마치기 성공 !")
                        findNavController().popBackStack()
                    }

                    is UiState.Failure -> {
                        toast(patchMeetUpState.errorMessage)
                    }

                    else -> Unit
                }
            }.launchIn(viewLifeCycleScope)
    }

    private fun initPenaltyState(latePersonModel: LatePersonModel) {
        binding.tvPenaltyDescription.text = latePersonModel.penalty
    }

    private fun setupMeetUpCompleteBtn() {
        binding.btnEndMeetUp.setOnClickListener {
            latePersonViewModel.patchMeetUpComplete(promiseId)
        }
    }

    private fun updateButtonState(isPastDue: Boolean) {
        binding.btnEndMeetUp.isEnabled = isPastDue
    }

    companion object {
        @JvmStatic
        fun newInstance(promiseId: Int) = LatePersonFragment().apply {
            arguments = Bundle().apply {
                putInt(KeyStorage.PROMISE_ID, promiseId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _latePersonAdapter = null
    }
}
