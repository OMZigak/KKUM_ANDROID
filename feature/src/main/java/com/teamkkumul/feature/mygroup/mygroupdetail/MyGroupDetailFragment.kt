package com.teamkkumul.feature.mygroup.mygroupdetail

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
import com.teamkkumul.feature.databinding.FragmentMyGroupDetailBinding
import com.teamkkumul.feature.mygroup.mygroupdetail.adapter.MyGroupDetailFriendAdapter
import com.teamkkumul.feature.mygroup.mygroupdetail.adapter.MyGroupDetailMeetUpAdapter
import com.teamkkumul.feature.utils.itemdecorator.MeetUpFriendItemDecoration
import com.teamkkumul.model.MyGroupInfoModel
import com.teamkkumul.model.MyGroupMemberModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MyGroupDetailFragment :
    BindingFragment<FragmentMyGroupDetailBinding>(R.layout.fragment_my_group_detail) {
    private val viewModel: MyGroupDetailViewModel by viewModels()

    private var _memberAdapter: MyGroupDetailFriendAdapter? = null
    private val memberAdapter get() = requireNotNull(_memberAdapter)

    private var _meetUpAdapter: MyGroupDetailMeetUpAdapter? = null
    private val meetUpAdapter get() = requireNotNull(_meetUpAdapter)

    override fun initView() {
        initMemberRecyclerView()
        initObserveMemberListState()
        initMeetUpRecyclerView()
//        initObserveMeetUpState()
        initObserveMyGroupMeetUpState()

        binding.extendedFab.setOnClickListener {
            findNavController().navigate(R.id.action_myGroupDetailFragment_to_meetUpCreateFragment)
        }
    }

    private fun successMyGroupInfoState(myGroupInfoModel: MyGroupInfoModel) {
        binding.tvMyGroupCreateDate.text = myGroupInfoModel.createdAt
        binding.tvMyGroupMeetUpCount.text = myGroupInfoModel.metCount.toString()
    }

    private fun successMyGroupMember(myGroupMemberModel: MyGroupMemberModel) {
        with(binding) {
            tvMyGroupParticipatePeopleCount.text = myGroupMemberModel.memberCount.toString()
        }
    }

    private fun initObserveMemberListState() {
        viewModel.myGroupMemberListState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Failure -> Timber.tag("my group member list").d(uiState.errorMessage)
                is UiState.Success -> {}
                is UiState.Empty -> {}

                else -> {}
            }
        }
    }

    private fun initObserveMyGroupMeetUpState() {
        viewModel.myGroupMeetUpState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Failure -> Timber.tag("my group meet up list").d(uiState.errorMessage)
                is UiState.Success -> {
                    updateMeetingVisibility(true)
                    meetUpAdapter.submitList(uiState.data)
                }

                is UiState.Empty -> updateMeetingVisibility(false)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initMemberRecyclerView() {
        _memberAdapter = MyGroupDetailFriendAdapter(
            onPlusBtnClicked = { findNavController().navigate(R.id.exampleComposeFragment) }, // 임시로 이동하는 페이지
        )
        binding.rvMyGroupFriendList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = memberAdapter
            addItemDecoration(MeetUpFriendItemDecoration(requireContext()))
        }
    }

    private fun initMeetUpRecyclerView() {
        _meetUpAdapter = MyGroupDetailMeetUpAdapter(
            onMeetUpDetailBtnClicked = {
                findNavController().navigate(R.id.exampleComposeFragment) // 임시로 이동하는 페이지
            },
        )
        binding.rvMyGroupMeetUp.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = meetUpAdapter
            addItemDecoration(MeetUpFriendItemDecoration(requireContext()))
        }
    }

    private fun updateMeetingVisibility(isVisible: Boolean) {
        binding.rvMyGroupMeetUp.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.viewMyGroupMeetUpEmpty.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _memberAdapter = null
        _meetUpAdapter = null
    }
}
