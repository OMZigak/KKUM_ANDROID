package com.teamkkumul.feature.mygroup.mygroupdetail

import android.view.View
import androidx.core.os.bundleOf
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
import com.teamkkumul.feature.newgroup.addnewgroup.DialogInvitationCodeFragment
import com.teamkkumul.feature.utils.KeyStorage.MEETING_ID
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
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

    private var code: String = ""
    private var currentId: Int = -1

    override fun initView() {
        val id = arguments?.getInt(MEETING_ID) ?: -1

        if (id != -1) {
            currentId = id
        }

        initMemberRecyclerView()
        initMeetUpRecyclerView()
        viewModel.getMyGroupInfo(currentId)
        viewModel.getMyGroupMember(currentId)
        viewModel.getMyGroupMeetUp(currentId, false)
        viewModel.getMyGroupMemberList(currentId)

        initObserveMyGroupInfoState()
        initObserveMemberState()
        initObserveMyGroupMeetUpState()
        initObserveMemberListState()
        initObserveMyGroupInfoState()

        binding.extendedFab.setOnClickListener {
            findNavController().navigate(
                R.id.action_myGroupDetailFragment_to_meetUpCreateFragment,
                bundleOf(MEETING_ID to currentId),
            )
        }
    }

    private fun initObserveMyGroupInfoState() {
        viewModel.myGroupInfoState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    successMyGroupInfoState(uiState.data)
                    code = uiState.data.invitationCode
                }

                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun successMyGroupInfoState(myGroupInfoModel: MyGroupInfoModel) {
        binding.tvMyGroupCreateDate.text = myGroupInfoModel.createdAt
        binding.tvMyGroupMeetUpCount.text = myGroupInfoModel.metCount.toString()
    }

    private fun initObserveMemberState() {
        viewModel.myGroupMemberState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    successMyGroupMemberState(uiState.data)
                }

                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun successMyGroupMemberState(myGroupMemberModel: MyGroupMemberModel) {
        with(binding) {
            tvMyGroupParticipatePeopleCount.text = myGroupMemberModel.memberCount.toString()
        }
    }

    private fun initObserveMemberListState() {
        viewModel.myGroupMemberListState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    memberAdapter.submitList(uiState.data)
                }

                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initObserveMyGroupMeetUpState() {
        viewModel.myGroupMeetUpState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Failure -> Timber.tag("my group meet up list").d(uiState.errorMessage)
                is UiState.Success -> {
                    if (uiState.data.isEmpty()) {
                        updateMeetingVisibility(false)
                    } else {
                        updateMeetingVisibility(true)
                        meetUpAdapter.submitList(uiState.data)
                    }
                }

                is UiState.Empty -> updateMeetingVisibility(false)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initMemberRecyclerView() {
        _memberAdapter = MyGroupDetailFriendAdapter(
            onPlusBtnClicked = {
                findNavController().navigate(
                    R.id.fragment_dialog_invitation_code,
                    bundleOf("code" to code),
                )
               /* val dialog = DialogInvitationCodeFragment.newInstance(
                    code,
                )
                dialog.show(parentFragmentManager, "DialogInvitationCodeFragment")*/
            },
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
            onMeetUpDetailBtnClicked = { promiseId ->
                findNavController().navigate(
                    R.id.action_myGroupDetailFragment_to_meetUpContainerFragment,
                    bundleOf(PROMISE_ID to promiseId),
                )
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
