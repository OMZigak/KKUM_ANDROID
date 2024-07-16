package com.teamkkumul.feature.meetup.lateperson

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.pxToDp
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentLatePersonBinding
import com.teamkkumul.feature.utils.itemdecorator.GridSpacingItemDecoration
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LatePersonFragment :
    BindingFragment<FragmentLatePersonBinding>(R.layout.fragment_late_person) {

    private val latePersonViewModel: LatePersonViewModel by viewModels()

    private var _latePersonAdapter: LatePersonAdapter? = null
    private val latePersonAdapter get() = requireNotNull(_latePersonAdapter)
    override fun initView() {
        initRecyclerView()
        initObserveGroupState()
    }

    private fun initRecyclerView() {
        _latePersonAdapter = LatePersonAdapter()
        binding.rvLatePerson.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = latePersonAdapter
            addItemDecoration(GridSpacingItemDecoration(3, requireContext().pxToDp(8)))
        }
    }

    private fun initObserveGroupState() {
        latePersonViewModel.latePerson.flowWithLifecycle(lifecycle).onEach { latePerson ->
            if (latePerson.isEmpty()) {
                binding.viewLatePersonEmpty.visibility = View.VISIBLE
                binding.rvLatePerson.visibility = View.GONE
            } else {
                binding.rvLatePerson.visibility = View.VISIBLE
                binding.viewLatePersonEmpty.visibility = View.GONE
                latePersonAdapter.submitList(latePerson)
            }
        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _latePersonAdapter = null
    }
}
