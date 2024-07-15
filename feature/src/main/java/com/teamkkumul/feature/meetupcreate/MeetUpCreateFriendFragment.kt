package com.teamkkumul.feature.meetupcreate

import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.pxToDp
import com.teamkkumul.core.ui.util.fragment.toast
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpFriendPlusBinding
import com.teamkkumul.feature.utils.itemdecorator.GridSpacingItemDecoration
import com.teamkkumul.feature.utils.animateProgressBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeetUpCreateFriendFragment :
    BindingFragment<FragmentMeetUpFriendPlusBinding>(R.layout.fragment_meet_up_friend_plus) {

    private val viewModel: MeetUpCreateViewModel by activityViewModels<MeetUpCreateViewModel>()

    private var _friendAdapter: MeetUpCreateFriendAdapter? = null
    private val friendAdapter get() = requireNotNull(_friendAdapter)

    override fun initView() {
        viewModel.setProgressBar(50)

        initRecyclerView()
        initObserveViewModel()
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
                checkSelectedItems(selectedItem)
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

    private fun checkSelectedItems(selectedItems: List<Int>) {
        toast("Selected items: $selectedItems")
    }

    private fun updateNextButton(isEnabled: Boolean) {
        with(binding.tvMeetUpFriendPlusNext) {
            this.isEnabled = isEnabled
            if (isEnabled) {
                setOnClickListener {
                    findNavController().navigate(R.id.compose_example) // 이동할 페이지
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

    private fun initObserveViewModel() {
        viewModel.members.observe(viewLifecycleOwner) { members ->
            friendAdapter.submitList(members)
        }
    }

    private fun initNextButton() {
        updateNextButton(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _friendAdapter = null
    }
}
