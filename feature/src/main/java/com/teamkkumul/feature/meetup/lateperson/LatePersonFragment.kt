package com.teamkkumul.feature.meetup.lateperson

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.pxToDp
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentLatePersonBinding
import com.teamkkumul.feature.utils.itemdecorator.GridSpacingItemDecoration
import kotlinx.coroutines.launch

class LatePersonFragment :
    BindingFragment<FragmentLatePersonBinding>(R.layout.fragment_late_person) {

    private val viewModel: LatePersonViewModel by viewModels()

    private var _latePersonAdapter: LatePersonAdapter? = null
    private val latePersonAdapter get() = requireNotNull(_latePersonAdapter)
    override fun initView() {
        initRecyclerView()
        observeViewModel()
    }

    private fun initRecyclerView() {
        _latePersonAdapter = LatePersonAdapter()
        binding.rvLatePerson.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = latePersonAdapter
            addItemDecoration(GridSpacingItemDecoration(3, requireContext().pxToDp(8)))
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.latePerson.collect { latePerson ->
                latePersonAdapter.submitList(latePerson)
            }
        }
    }
}
