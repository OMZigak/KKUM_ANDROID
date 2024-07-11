package com.teamkkumul.feature.mygroup

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMyGroupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyGroupFragment : BindingFragment<FragmentMyGroupBinding>(R.layout.fragment_my_group) {
    private val myGroupViewModel: MyGroupViewModel by viewModels()
    private var _memberAdapter: MyGroupAdapter? = null
    private val memberAdapter get() = requireNotNull(_memberAdapter)

    override fun initView() {
        initGroupRecyclerView()
        initObserveGroupState()
    }

    private fun initObserveGroupState() {
        myGroupViewModel.group.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Empty -> {
                    binding.viewMyGroupEmpty.visibility = View.VISIBLE
                    binding.rvMyGroupList.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.rvMyGroupList.visibility = View.VISIBLE
                    binding.viewMyGroupEmpty.visibility = View.GONE
                    memberAdapter.submitList(it.data)
                }

                else -> {}
            }
        }
    }

    private fun initGroupRecyclerView() {
        _memberAdapter = MyGroupAdapter(
            myGroupListBtnClicked = {
                findNavController().navigate(R.id.exampleComposeFragment)
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
