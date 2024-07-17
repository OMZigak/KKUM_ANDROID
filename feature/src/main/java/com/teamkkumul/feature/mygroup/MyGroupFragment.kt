package com.teamkkumul.feature.mygroup

import android.view.View
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
        initObserveMyGroupState()
        viewModel.getMyGroupList()
        viewModel.getName()
        initGetName()
    }

    private fun initGetName() {
        binding.tvMyGroupName.text = viewModel.userName
    }

    private fun initObserveMyGroupListState() {
        viewModel.myGroupState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    successState(uiState.data)
                }

                is UiState.Failure -> Timber.tag("my group list").d(uiState.errorMessage)
                else -> {}
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initObserveMyGroupState() {
        viewModel.myGroupListState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Empty -> {
                    updateMyGroupVisibility(false)
                }

                is UiState.Failure -> Timber.tag("my group count").d(uiState.errorMessage)
                is UiState.Success -> {
                    updateMyGroupVisibility(true)
                    memberAdapter.submitList(uiState.data)
                }

                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun successState(myGroupModel: MyGroupModel) {
        with(binding) {
            tvMyGroupTotalCount.text = myGroupModel.count.toString()
        }
    }

    private fun updateMyGroupVisibility(isVisible: Boolean) {
        binding.rvMyGroupList.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.viewMyGroupEmpty.visibility = if (isVisible) View.GONE else View.VISIBLE
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
