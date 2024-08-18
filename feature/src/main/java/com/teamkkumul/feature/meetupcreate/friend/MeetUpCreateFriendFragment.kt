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
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpFriendPlusBinding
import com.teamkkumul.feature.meetupcreate.MeetUpCreateViewModel
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.KeyStorage.MEETING_ID
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

    private var _friendAdapter: MeetUpCreateFriendAdapter? = null
    private val friendAdapter get() = requireNotNull(_friendAdapter)

    private var selectedItems: MutableList<Int> = mutableListOf()

    private var meetingId: Int = -1
    private lateinit var meetUpDate: String
    private lateinit var meetUpTime: String
    private lateinit var meetUpLocation: String
    private lateinit var meetUpName: String
    private lateinit var meetUpLocationX: String
    private lateinit var meetUpLocationY: String

    override fun initView() {
        arguments?.let {
            meetingId = it.getInt(MEETING_ID, -1)
            meetUpDate = it.getString(KeyStorage.MEET_UP_DATE, "")
            meetUpTime = it.getString(KeyStorage.MEET_UP_TIME, "")
            meetUpLocation = it.getString(KeyStorage.MEET_UP_LOCATION, "")
            meetUpName = it.getString(KeyStorage.MEET_UP_NAME, "")
            meetUpLocationX = it.getString(KeyStorage.MEET_UP_LOCATION_X, "")
            meetUpLocationY = it.getString(KeyStorage.MEET_UP_LOCATION_Y, "")
        }

        viewModel.setProgressBar(50)
        binding.tbMeetUpCreate.toolbarMyPageLine.visibility = View.GONE
        binding.tbMeetUpCreate.ivBtnMore.visibility = View.GONE

        initRecyclerView()
        viewModel.getMyGroupMemberToMeetUp(meetingId)
        initObserveMyGroupMemberToMeetUp()
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
        _friendAdapter = MeetUpCreateFriendAdapter(
            { selectedItem ->
                selectedItems = selectedItem.toMutableList()
                Timber.tag("sss").d(selectedItem.toString())
            },
            { isSelected ->
                updateNextButton(isSelected)
            },
        )
        binding.rvMeetUpCreateFreindPlus.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = friendAdapter
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
                    friendAdapter.submitList(uiState.data)
                }

                is UiState.Failure -> Timber.tag("meet up create friend").d(uiState.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initNextButton() {
        updateNextButton(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _friendAdapter = null
    }
}
