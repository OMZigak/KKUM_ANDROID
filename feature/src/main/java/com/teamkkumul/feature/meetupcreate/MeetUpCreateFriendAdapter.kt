package com.teamkkumul.feature.meetupcreate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemMeetUpCreateFriendBinding
import com.teamkkumul.feature.meetupcreate.viewholder.MeetUpCreateFriendViewHolder
import com.teamkkumul.model.MeetUpSealedItem

class MeetUpCreateFriendAdapter(
    private val onMeetUpCreateFriendClicked: (List<Int>) -> Unit,
    private val onMeetUpCreateFriendSelected: (Boolean) -> Unit,
) :
    ListAdapter<MeetUpSealedItem.Participant, MeetUpCreateFriendViewHolder>(DiffUtil) {

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
        val isSelected = selectedItem.contains(position)
        holder.onBind(item, isSelected)

        holder.itemView.setOnClickListener {
            if (isSelected) {
                selectedItem.remove(position)
            } else {
                selectedItem.add(position)
            }
            notifyItemChanged(position)
            onMeetUpCreateFriendClicked(selectedItem.toList())
            onMeetUpCreateFriendSelected(selectedItem.isNotEmpty())
        }
    }

    companion object {
        private val DiffUtil = ItemDiffCallback<MeetUpSealedItem.Participant>(
            onItemsTheSame = { old, new -> old.id == new.id },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
