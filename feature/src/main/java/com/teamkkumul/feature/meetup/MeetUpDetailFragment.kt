package com.teamkkumul.feature.meetup

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpDetailBinding
import com.teamkkumul.feature.mygroup.MeetUpDetailListAdapter
import com.teamkkumul.feature.mygroup.MyGroupMeetUpItemDecoration
import com.teamkkumul.model.MeetUpSealedItem

class MeetUpDetailFragment :
    BindingFragment<FragmentMeetUpDetailBinding>(R.layout.fragment_meet_up_detail) {
    private val groupFriendViewModel: MeetUpDetailFriendViewModel by viewModels()

    private var _memberAdapter: MeetUpDetailListAdapter? = null
    private val memberAdapter get() = requireNotNull(_memberAdapter)

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
            addItemDecoration(MyGroupMeetUpItemDecoration(requireContext()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _memberAdapter = null
    }
}
