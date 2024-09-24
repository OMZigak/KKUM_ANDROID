package com.teamkkumul.feature.meetupcreate.friend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemMeetUpCreateFriendBinding
import com.teamkkumul.feature.meetupcreate.friend.viewholder.MeetUpEditFriendViewHolder
import com.teamkkumul.model.MeetUpEditParticipantModel
import timber.log.Timber

class MeetUpEditFriendAdapter(
    private val onMeetUpCreateFriendClicked: (List<Int>) -> Unit,
    private val onMeetUpCreateFriendSelected: (Boolean) -> Unit,
) : ListAdapter<MeetUpEditParticipantModel.Member, MeetUpEditFriendViewHolder>(DiffUtil) {

    private val selectedItem = mutableSetOf<Int>()

    init {
        currentList.forEach { member ->
            if (member.isParticipant) {
                selectedItem.add(member.memberId)
            }
        }
    }

    override fun submitList(list: List<MeetUpEditParticipantModel.Member>?) {
        super.submitList(list)
        selectedItem.clear()
        list?.forEach {
            if (it.isParticipant) {
                selectedItem.add(it.memberId)
            }
        }
        onMeetUpCreateFriendClicked(selectedItem.toList())
        onMeetUpCreateFriendSelected(selectedItem.isNotEmpty())
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MeetUpEditFriendViewHolder {
        val binding = ItemMeetUpCreateFriendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return MeetUpEditFriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeetUpEditFriendViewHolder, position: Int) {
        val item = getItem(position)
        val isSelected = selectedItem.contains(item.memberId)
        holder.onBind(item, isSelected)
        Timber.tag("sel adapter").d(isSelected.toString())

        holder.itemView.setOnClickListener {
            if (isSelected) {
                selectedItem.remove(item.memberId)
            } else {
                selectedItem.add(item.memberId)
            }
            notifyItemChanged(position)
            onMeetUpCreateFriendClicked(selectedItem.toList())
            onMeetUpCreateFriendSelected(selectedItem.isNotEmpty())
        }
    }

    companion object {
        private val DiffUtil = ItemDiffCallback<MeetUpEditParticipantModel.Member>(
            onItemsTheSame = { old, new ->
                old.memberId == new.memberId
            },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
