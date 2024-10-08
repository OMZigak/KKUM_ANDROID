package com.teamkkumul.feature.mygroup.mygroupdetail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.core.ui.view.setInVisible
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMyGroupDetailBinding
import com.teamkkumul.feature.mygroup.mygroupdetail.adapter.MyGroupDetailFriendAdapter
import com.teamkkumul.feature.mygroup.mygroupdetail.adapter.MyGroupDetailMeetUpAdapter
import com.teamkkumul.feature.utils.KeyStorage.ADD_NEW_GROUP_MODEL
import com.teamkkumul.feature.utils.KeyStorage.D_DAY
import com.teamkkumul.feature.utils.KeyStorage.GROUP_NAME
import com.teamkkumul.feature.utils.KeyStorage.MEETING_ID
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.itemdecorator.MeetUpFriendItemDecoration
import com.teamkkumul.feature.utils.time.TimeUtils.parseDateToYearMonthDay
import com.teamkkumul.model.AddNewGroupModel
import com.teamkkumul.model.MyGroupInfoModel
import com.teamkkumul.model.MyGroupMemberModel
import com.teamkkumul.model.type.ScreenType
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
    private val currentId: Int by lazy { arguments?.getInt(MEETING_ID, -1) ?: -1 }

    override fun initView() {
        initMemberRecyclerView()
        initMeetUpRecyclerView()
        viewModel.getMyGroupInfo(currentId)
        viewModel.getMyGroupMember(currentId)
        viewModel.getMyGroupMeetUp(currentId, false, true)
        viewModel.getMyGroupMemberList(currentId)

        initObserveMyGroupInfoState()
        initObserveMemberState()
        initObserveMyGroupMeetUpState()
        initObserveMemberListState()
        initObserveMyGroupInfoState()

        navigationClickListeners()
        textInitialState()
        textClickListeners()

        binding.toolbarMyGroupDetail.ivBtnMore.visibility = View.VISIBLE
    }

    private fun textClickListeners() {
        binding.clAllMeetUp.setOnClickListener {
            switchToAllMeetUpState()
        }

        binding.clMeetUpIncludeMe.setOnClickListener {
            switchToMeetUpIncludeMeState()
        }
    }

    private fun navigationClickListeners() {
        binding.extendedFab.setOnClickListener {
            findNavController().navigate(
                R.id.action_myGroupDetailFragment_to_meetUpCreateFragment,
                bundleOf(MEETING_ID to currentId),
            )
            Timber.tag("checkeeee").d(currentId.toString())
        }

        binding.toolbarMyGroupDetail.ivBtnMore.setOnClickListener {
            val groupName = binding.toolbarMyGroupDetail.title.toString()
            findNavController().navigate(
                R.id.action_myGroupDetailFragment_to_exitBottomSheetFragment,
                bundleOf(MEETING_ID to currentId, GROUP_NAME to groupName),
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
        binding.tvMyGroupCreateDate.text = myGroupInfoModel.createdAt.parseDateToYearMonthDay()
        binding.tvMyGroupMeetUpCount.text = myGroupInfoModel.metCount.toString()
        binding.toolbarMyGroupDetail.title = myGroupInfoModel.name
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
                val addNewGroupModel = AddNewGroupModel(
                    meetingId = currentId,
                    invitationCode = code,
                    screenType = ScreenType.MY_GROUP_DETAIL,
                )
                findNavController().navigate(
                    R.id.fragment_dialog_invitation_code,
                    bundleOf(ADD_NEW_GROUP_MODEL to addNewGroupModel),
                )
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
            onMeetUpDetailBtnClicked = { item ->
                findNavController().navigate(
                    R.id.action_myGroupDetailFragment_to_meetUpContainerFragment,
                    bundleOf(
                        PROMISE_ID to item.promiseId,
                        D_DAY to item.dDay,
                    ),
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

    private fun textInitialState() {
        updateTextAppearance(true)
        updateTextVisibility(true)
        binding.tvAllMeetUp.setTextColor(colorOf(R.color.gray6))
    }

    private fun switchToAllMeetUpState() {
        viewModel.updateMeetUpIncludeMeState(false)
        updateTextAppearance(false)
        updateTextVisibility(false)
        binding.tvMeetUpIncludeMe.setTextColor(colorOf(R.color.gray6))
        viewModel.getMyGroupMeetUp(currentId, false)
    }

    private fun switchToMeetUpIncludeMeState() {
        viewModel.updateMeetUpIncludeMeState(true)
        updateTextAppearance(true)
        updateTextVisibility(true)
        binding.tvAllMeetUp.setTextColor((colorOf(R.color.gray6)))
        viewModel.getMyGroupMeetUp(currentId, false, true)
    }

    private fun updateTextVisibility(isVisible: Boolean) {
        binding.vMeetUpIncludeMe.setInVisible(isVisible)
        binding.vAllMeetUp.setInVisible(!isVisible)
    }

    private fun updateTextAppearance(isSelected: Boolean) {
        val selectIncludeMe =
            if (isSelected) R.style.TextAppearance_Kkumul_body_05 else R.style.TextAppearance_Kkumul_body_06
        val selectAllMeetUp =
            if (isSelected) R.style.TextAppearance_Kkumul_body_06 else R.style.TextAppearance_Kkumul_body_05

        binding.tvMeetUpIncludeMe.setTextAppearance(selectIncludeMe)
        binding.tvAllMeetUp.setTextAppearance(selectAllMeetUp)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isMeetUpIncludeMeSelected.flowWithLifecycle(viewLifeCycle).onEach { isSelected ->
            if (isSelected) {
                switchToMeetUpIncludeMeState()
            } else {
                switchToAllMeetUpState()
            }
        }.launchIn(viewLifeCycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _memberAdapter = null
        _meetUpAdapter = null
    }
}
