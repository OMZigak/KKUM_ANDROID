package com.teamkkumul.feature.meetupcreate.friend

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.pxToDp
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.core.ui.view.setVisible
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpFriendPlusBinding
import com.teamkkumul.feature.meetupcreate.MeetUpCreateFriendViewModel
import com.teamkkumul.feature.meetupcreate.MeetUpSharedViewModel
import com.teamkkumul.feature.utils.animateProgressBar
import com.teamkkumul.feature.utils.itemdecorator.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MeetUpCreateFriendFragment :
    BindingFragment<FragmentMeetUpFriendPlusBinding>(R.layout.fragment_meet_up_friend_plus) {
    private val sharedViewModel: MeetUpSharedViewModel by activityViewModels<MeetUpSharedViewModel>()
    private val viewModel: MeetUpCreateFriendViewModel by activityViewModels<MeetUpCreateFriendViewModel>()
    private var _friendEditAdapter: MeetUpEditFriendAdapter? = null
    private val friendEditAdapter get() = requireNotNull(_friendEditAdapter)

    private var _friendCreateAdapter: MeetUpCreateFriendAdapter? = null
    private val friendCreateAdapter get() = requireNotNull(_friendCreateAdapter)

    private var selectedItems: MutableList<Int> = mutableListOf()

    override fun initView() {
        if (sharedViewModel.isEditMode()) initSetEditMemberUI(sharedViewModel.getPromiseId())
        else initSetDefaultMemberUI(sharedViewModel.getMeetingId())

        updateTobBarUi()
        initNextButton()
        observeProgress()
    }

    private fun initSetEditMemberUI(promiseId: Int) {
        viewModel.getMeetUpEditMember(promiseId) // viewModel.getPromiseId() =  promise id를 가져옴
        initObserveMeetUpEditParticipant()
        initRecyclerEditView()
    }

    private fun initSetDefaultMemberUI(meetingId: Int) {
        viewModel.getMyGroupMemberToMeetUp(meetingId)
        initObserveMyGroupMemberToMeetUp()
        initRecyclerView()
    }

    private fun updateTobBarUi() {
        sharedViewModel.setProgressBar(50)
        binding.tbMeetUpCreate.toolbarMyPageLine.visibility = View.GONE
    }

    private fun observeProgress() {
        val progressBar = binding.pbMeetUpCreateFriend
        sharedViewModel.progressLiveData.observe(viewLifecycleOwner) { progress ->
            progressBar.progress = progress
            animateProgressBar(progressBar, 25, progress)
        }
    }

    private fun initRecyclerView() {
        _friendCreateAdapter = MeetUpCreateFriendAdapter(
            { selectedItem ->
                selectedItems = selectedItem.toMutableList()
                Timber.tag("sss").d(selectedItem.toString())
            },
            { isSelected ->
                updateNextButton(true)
            },
        )
        binding.rvMeetUpCreateFreindPlus.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = friendCreateAdapter
            addItemDecoration(GridSpacingItemDecoration(3, requireContext().pxToDp(8)))
        }
    }

    private fun initRecyclerEditView() {
        _friendEditAdapter = MeetUpEditFriendAdapter(
            onMeetUpCreateFriendClicked = { selectedItem ->
                selectedItems = selectedItem.toMutableList()
                Timber.tag("sss").d(selectedItem.toString())
            },
            onMeetUpCreateFriendSelected = {
                updateNextButton(true)
            },
        )
        binding.rvMeetUpCreateFreindPlus.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = friendEditAdapter
            addItemDecoration(GridSpacingItemDecoration(3, requireContext().pxToDp(8)))
        }
    }

    private fun updateNextButton(isEnabled: Boolean) {
        with(binding.btnMeetUpCreateNext) {
            this.isEnabled = isEnabled
            if (isEnabled) {
                setOnClickListener {
                    sharedViewModel.updateMeetUpModel(
                        participants = selectedItems,
                    )
                    findNavController().navigate(
                        R.id.action_fragment_meet_up_create_friend_to_fragment_meet_up_level,
                    )
                }
            }
        }
    }

    private fun initObserveMyGroupMemberToMeetUp() {
        viewModel.meetUpCreateMemberState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    if (uiState.data.isEmpty()) {
                        updateMeetUpCreateFriendVisibility(false)
                    } else {
                        updateMeetUpCreateFriendVisibility(true)
                        friendCreateAdapter.submitList(uiState.data)
                    }
                }

                is UiState.Failure -> Timber.tag("meet up create friend").d(uiState.errorMessage)
                is UiState.Empty -> {
                    updateMeetUpCreateFriendVisibility(false)
                }

                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initObserveMeetUpEditParticipant() {
        viewModel.meetUpEditMemberState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    if (uiState.data == null) {
                        updateMeetUpCreateFriendVisibility(false)
                    } else {
                        updateMeetUpCreateFriendVisibility(true)
                        friendEditAdapter.submitList(uiState.data)
                    }
                }

                is UiState.Failure -> Timber.tag("meet up edit participant").d(uiState.errorMessage)
                is UiState.Empty -> updateMeetUpCreateFriendVisibility(false)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun updateMeetUpCreateFriendVisibility(isVisible: Boolean) {
        with(binding) {
            rvMeetUpCreateFreindPlus.setVisible(isVisible)
            viewMeetUpCreateFriendEmpty.setVisible(!isVisible)
        }
    }

    private fun initNextButton() {
        updateNextButton(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _friendEditAdapter = null
    }
}
