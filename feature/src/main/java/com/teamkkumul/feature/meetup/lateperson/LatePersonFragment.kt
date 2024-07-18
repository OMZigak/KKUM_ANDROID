package com.teamkkumul.feature.meetup.lateperson

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.pxToDp
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentLatePersonBinding
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.itemdecorator.GridSpacingItemDecoration
import com.teamkkumul.model.LatePersonModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

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
        latePersonViewModel.getLateComersList(promiseId)
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
                    is UiState.Success -> {
                        Timber.tag("aa").d(latePersonState.data.penalty.toString())
                        initPenaltyState(latePersonState.data)
                        val lateComers = latePersonState.data.lateComers
                        if (lateComers.isEmpty()) {
                            binding.viewLatePersonEmpty.visibility = View.VISIBLE
                            binding.rvLatePerson.visibility = View.GONE
                        } else {
                            binding.rvLatePerson.visibility = View.VISIBLE
                            binding.viewLatePersonEmpty.visibility = View.GONE
                            latePersonAdapter.submitList(lateComers)
                        }
                    }

                    is UiState.Failure -> Timber.tag("aa")
                        .d(latePersonState.errorMessage)

                    is UiState.Loading -> {}
                    else -> Unit
                }
            }.launchIn(viewLifeCycleScope)
    }

    private fun initPenaltyState(latePersonModel: LatePersonModel) {
        binding.tvPenaltyDescription.text = latePersonModel.penalty
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
