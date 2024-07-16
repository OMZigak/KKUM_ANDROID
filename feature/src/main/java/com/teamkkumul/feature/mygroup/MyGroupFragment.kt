package com.teamkkumul.feature.mygroup

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMyGroupBinding
import com.teamkkumul.feature.mygroup.adapter.MyGroupAdapter
import com.teamkkumul.model.MyGroupModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MyGroupFragment : BindingFragment<FragmentMyGroupBinding>(R.layout.fragment_my_group) {
    private val viewModel: MyGroupViewModel by viewModels()
    private var _memberAdapter: MyGroupAdapter? = null
    private val memberAdapter get() = requireNotNull(_memberAdapter)

    override fun initView() {
        initGroupRecyclerView()
        initObserveUiState()
        viewModel.getMyGroupList()
    }

    private fun initObserveUiState() {
        viewModel.myGroupState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Empty -> emptyState()
                is UiState.Loading -> loadingState()
                is UiState.Success -> successState(uiState.data)
                is UiState.Failure -> errorState(uiState.errorMessage)
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun emptyState() {
        with(binding) {
            viewMyGroupEmpty.visibility = View.VISIBLE
            rvMyGroupList.visibility = View.GONE
        }
    }

    private fun loadingState() {
        with(binding) {
            viewMyGroupEmpty.visibility = View.GONE
            rvMyGroupList.visibility = View.GONE
        }
    }

    private fun successState(myGroupModel: MyGroupModel) {
        with(binding) {
            viewMyGroupEmpty.visibility = View.GONE
            rvMyGroupList.visibility = View.VISIBLE
        }
        memberAdapter.submitList(myGroupModel.meetings)
    }

    private fun errorState(errorMessage: String) {
        with(binding) {
            viewMyGroupEmpty.visibility = View.VISIBLE
            rvMyGroupList.visibility = View.GONE
        }
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun initGroupRecyclerView() {
        binding.clMyGroupPlus.setOnClickListener {
            findNavController().navigate(R.id.action_myGroupFragment_to_addMyGroupFragment)
        }
        _memberAdapter = MyGroupAdapter(
            onMyGroupListBtnClicked = {
                findNavController().navigate(R.id.action_myGroupFragment_to_myGroupDetailFragment)
            },
        )
        binding.rvMyGroupList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = memberAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _memberAdapter = null
    }
}
