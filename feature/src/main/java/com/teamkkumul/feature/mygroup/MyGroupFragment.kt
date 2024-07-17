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
import timber.log.Timber

@AndroidEntryPoint
class MyGroupFragment : BindingFragment<FragmentMyGroupBinding>(R.layout.fragment_my_group) {
    private val viewModel: MyGroupViewModel by viewModels()
    private var _memberAdapter: MyGroupAdapter? = null
    private val memberAdapter get() = requireNotNull(_memberAdapter)

    override fun initView() {
        initGroupRecyclerView()
        initObserveMyGroupListState()
        obserbe()
        viewModel.getMyGroupList()
    }

    private fun initObserveMyGroupListState() {
        viewModel.myGroupState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    updateVisibility(isEmpty = false)
                    successState(uiState.data)
                }

                is UiState.Failure -> errorState(uiState.errorMessage)
                else -> {}
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun obserbe() {
        viewModel.myGroupListState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Empty -> {
                    emptyState()
                }

                is UiState.Failure -> Timber.tag("my gropu").d(it.errorMessage)
                is UiState.Success -> {
//                    binding.viewMyGroupEmpty.visibility = View.GONE
//                    binding.rvMyGroupList.visibility = View.VISIBLE
                    updateVisibility(true)
                    memberAdapter.submitList(it.data)
                }

                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun emptyState() {
        with(binding) {
            viewMyGroupEmpty.visibility = View.VISIBLE
            rvMyGroupList.visibility = View.GONE
        }
    }

    // ui도그리고 다 그리니까 visibitluty 만 따로 함수로 빼라
    private fun successState(myGroupModel: MyGroupModel) {
        with(binding) {
            tvMyGroupTotalCount.text = myGroupModel.count.toString()
        }
    }

    private fun errorState(errorMessage: String) {
        updateVisibility(isEmpty = true)
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun updateVisibility(isEmpty: Boolean) {
        with(binding) {
            if (isEmpty) {
                viewMyGroupEmpty.visibility = View.VISIBLE
                rvMyGroupList.visibility = View.GONE
            } else {
                viewMyGroupEmpty.visibility = View.GONE
                rvMyGroupList.visibility = View.VISIBLE
            }
        }
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
