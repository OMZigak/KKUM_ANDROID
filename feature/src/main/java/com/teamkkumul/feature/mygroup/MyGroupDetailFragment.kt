package com.teamkkumul.feature.mygroup

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMyGroupDetailBinding
import com.teamkkumul.model.MyGroupSealedItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyGroupDetailFragment :
    BindingFragment<FragmentMyGroupDetailBinding>(R.layout.fragment_my_group_detail) {
    private val groupFriendViewModel: MyGroupFriendViewModel by viewModels()
    private val meetUpViewModel: MyGroupMeetUpViewModel by viewModels()

    private var _memberAdapter: MyGroupDetailListAdapter? = null
    private val memberAdapter get() = requireNotNull(_memberAdapter)

    private var _meetUpAdapter: MyGroupMeetUpListAdapter? = null
    private val meetUpAdapter get() = requireNotNull(_meetUpAdapter)

    override fun initView() {
        initMemberRecyclerView()
        initObserveMemberState()
        initMeetUpRecyclerView()
        initObserveMeetUpState()

        binding.extendedFab.setOnClickListener {
            findNavController().navigate(R.id.action_myGroupDetailFragment_to_meetUpDetailFragment)
        }
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
        _memberAdapter = MyGroupDetailListAdapter(
            onPlusBtnClicked = { findNavController().navigate(R.id.exampleComposeFragment) }, // 임시로 이동하는 페이지
        )
        binding.rvMyGroupFriendList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = memberAdapter
            addItemDecoration(MyGroupMeetUpItemDecoration(requireContext()))
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
        _meetUpAdapter = null
    }
}
