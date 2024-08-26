package com.teamkkumul.feature.meetupcreate.friend

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
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
import com.teamkkumul.feature.meetupcreate.MeetUpCreateViewModel
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.KeyStorage.MEETING_ID
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.MeetUpType
import com.teamkkumul.feature.utils.animateProgressBar
import com.teamkkumul.feature.utils.itemdecorator.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MeetUpCreateFriendFragment :
    BindingFragment<FragmentMeetUpFriendPlusBinding>(R.layout.fragment_meet_up_friend_plus) {

    private val viewModel: MeetUpCreateViewModel by activityViewModels<MeetUpCreateViewModel>()

    private var _friendEditAdapter: MeetUpEditFriendAdapter? = null
    private val friendEditAdapter get() = requireNotNull(_friendEditAdapter)

    private var _friendCreateAdapter: MeetUpCreateFriendAdapter? = null
    private val friendCreateAdapter get() = requireNotNull(_friendCreateAdapter)

    private var selectedItems: MutableList<Int> = mutableListOf()

    private var meetingId: Int = -1
    private var promiseId: Int = -1

    private lateinit var meetUpDate: String
    private lateinit var meetUpTime: String
    private lateinit var meetUpLocation: String
    private lateinit var meetUpName: String
    private lateinit var meetUpLocationX: String
    private lateinit var meetUpLocationY: String

    override fun initView() {
        arguments?.let {
            promiseId = it.getInt(PROMISE_ID, -1)
            meetingId = it.getInt(MEETING_ID, -1)
            meetUpDate = it.getString(KeyStorage.MEET_UP_DATE, "")
            meetUpTime = it.getString(KeyStorage.MEET_UP_TIME, "")
            meetUpLocation = it.getString(KeyStorage.MEET_UP_LOCATION, "")
            meetUpName = it.getString(KeyStorage.MEET_UP_NAME, "")
            meetUpLocationX = it.getString(KeyStorage.MEET_UP_LOCATION_X, "")
            meetUpLocationY = it.getString(KeyStorage.MEET_UP_LOCATION_Y, "")

            val meetUpType = it.getString(KeyStorage.MEET_UP_TYPE, MeetUpType.CREATE.toString())

            if (MeetUpType.valueOf(meetUpType) == MeetUpType.EDIT) {
                viewModel.getMeetUpEditMember(promiseId)
                initObserveMeetUpEditParticipant()
                initRecyclerEditView()
                Timber.d("Editing new meetup")
            } else {
                viewModel.getMyGroupMemberToMeetUp(meetingId)
                initObserveMyGroupMemberToMeetUp()
                initRecyclerView()
                Timber.d("Creating new meetup")
            }
        }

        viewModel.setProgressBar(50)
        binding.tbMeetUpCreate.toolbarMyPageLine.visibility = View.GONE

        initNextButton()
        observeProgress()
    }

    private fun observeProgress() {
        val progressBar = binding.pbMeetUpCreateFriend
        viewModel.progressLiveData.observe(viewLifecycleOwner) { progress ->
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
        with(binding.tvMeetUpFriendPlusNext) {
            this.isEnabled = isEnabled
            if (isEnabled) {
                setOnClickListener {
                    val bundle = arguments?.apply {
                        putIntArray("selectedItems", selectedItems.toIntArray())
                        putString(KeyStorage.MEET_UP_DATE, meetUpDate)
                        putString(KeyStorage.MEET_UP_TIME, meetUpTime)
                        putString(KeyStorage.MEET_UP_LOCATION, meetUpLocation)
                        putString(KeyStorage.MEET_UP_NAME, meetUpName)
                    }
                    findNavController().navigate(
                        R.id.action_fragment_meet_up_create_friend_to_fragment_meet_up_level,
                        bundle,
                    )
                    Timber.tag("edit friend").d(selectedItems.toString())
                }
                ViewCompat.setBackgroundTintList(
                    this,
                    ContextCompat.getColorStateList(requireContext(), R.color.main_color),
                )
            } else {
                setOnClickListener(null)
                ViewCompat.setBackgroundTintList(
                    this,
                    ContextCompat.getColorStateList(requireContext(), R.color.gray2),
                )
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
                        Timber.tag("meee").d(uiState.data.toString())
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
                        Timber.tag("meetupedit").d(uiState.data.toString())
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
