package com.teamkkumul.feature.meetupcreate.friend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemMeetUpCreateFriendBinding
import com.teamkkumul.model.MyGroupMemberModel

class MeetUpCreateFriendAdapter(
    private val onMeetUpCreateFriendClicked: (List<Int>) -> Unit,
    private val onMeetUpCreateFriendSelected: (Boolean) -> Unit,
) : ListAdapter<MyGroupMemberModel.Member, MeetUpCreateFriendViewHolder>(DiffUtil) {

    private val selectedItem = mutableSetOf<Int>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MeetUpCreateFriendViewHolder {
        val binding = ItemMeetUpCreateFriendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return MeetUpCreateFriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeetUpCreateFriendViewHolder, position: Int) {
        val item = getItem(position)
        val isSelected = selectedItem.contains(item.memberId)
        holder.onBind(item, isSelected)

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
        private val DiffUtil = ItemDiffCallback<MyGroupMemberModel.Member>(
            onItemsTheSame = { old, new -> old.memberId == new.memberId },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
