package com.teamkkumul.feature.mygroup

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMyGroupBinding
import com.teamkkumul.model.MyGroupSealedItem

class MyGroupFragment : BindingFragment<FragmentMyGroupBinding>(R.layout.fragment_my_group) {
    private val groupFriendViewModel: MyGroupFriendViewModel by viewModels()
    private val meetUpViewModel: MyGroupMeetUpViewModel by viewModels()

    private var _memberAdapter: MyGroupListAdapter? = null
    private val memberAdapter get() = requireNotNull(_memberAdapter)

    private var _meetUpAdapter: MyGroupMeetUpListAdapter? = null
    private val meetUpAdapter get() = requireNotNull(_meetUpAdapter)

    override fun initView() {
        initMemberRecyclerView()
        initObserveMemberState()
        initMeetUpRecyclerView()
        initObserveMeetUpState()
    }

    private fun initObserveMemberState() {
        groupFriendViewModel.members.observe(viewLifecycleOwner) {
            val newList = mutableListOf<MyGroupSealedItem>()
            newList.add(MyGroupSealedItem.MyGroupPlus(0))
            newList.addAll(it)
            memberAdapter.submitList(newList)
        }
    }

    private fun initObserveMeetUpState() {
        meetUpViewModel.promise.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Empty -> {
                    binding.viewMyGroupMeetUpEmpty.visibility = View.VISIBLE
                    binding.rvMyGroupMeetUp.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.rvMyGroupMeetUp.visibility = View.VISIBLE
                    binding.viewMyGroupMeetUpEmpty.visibility = View.GONE
                    meetUpAdapter.submitList(it.data)
                }
                else -> {}
            }
        }
    }

    private fun initMemberRecyclerView() {
        _memberAdapter = MyGroupListAdapter(
            onPlusBtnClicked = { findNavController().navigate(R.id.exampleComposeFragment) }, // 임시로 이동하는 페이지
        )
        binding.rvMyGroupFriendList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = memberAdapter
        }
    }

    private fun initMeetUpRecyclerView() {
        _meetUpAdapter = MyGroupMeetUpListAdapter(
            onMeetUpDetailBtnClicked = {
                findNavController().navigate(R.id.exampleComposeFragment) // 임시로 이동하는 페이지
            },
        )
        binding.rvMyGroupMeetUp.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = meetUpAdapter
            addItemDecoration(MyGroupMeetUpItemDecoration(requireContext()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _memberAdapter = null
    }
}
