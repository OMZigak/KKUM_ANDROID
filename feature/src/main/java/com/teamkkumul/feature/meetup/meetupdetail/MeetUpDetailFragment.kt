package com.teamkkumul.feature.meetup.meetupdetail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpDetailBinding
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.itemdecorator.MeetUpFriendItemDecoration
import com.teamkkumul.model.MeetUpSealedItem
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MeetUpDetailFragment :
    BindingFragment<FragmentMeetUpDetailBinding>(R.layout.fragment_meet_up_detail) {
    private val groupFriendViewModel: MeetUpDetailFriendViewModel by viewModels()

    private var _memberAdapter: MeetUpDetailListAdapter? = null
    private val memberAdapter get() = requireNotNull(_memberAdapter)

    private val promiseId: Int by lazy {
        requireArguments().getInt(PROMISE_ID)
    }

    override fun initView() {
        initMemberRecyclerView()
        initObserveMemberState()
    }

    private fun initObserveMemberState() {
        groupFriendViewModel.members.observe(viewLifecycleOwner) {
            val newList = mutableListOf<MeetUpSealedItem>()
            newList.add(MeetUpSealedItem.MyGroupPlus(0))
            newList.addAll(it)
            memberAdapter.submitList(newList)
        }
    }

    private fun initMemberRecyclerView() {
        _memberAdapter = MeetUpDetailListAdapter()
        binding.rvMeetUpFriendList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = memberAdapter
            addItemDecoration(MeetUpFriendItemDecoration(requireContext()))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(promiseId: Int) = MeetUpDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(PROMISE_ID, promiseId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _memberAdapter = null
    }
}
