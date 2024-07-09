package com.teamkkumul.feature.meetup.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.databinding.ItemMeetUpDetailFriendPlusBinding
import com.teamkkumul.model.MeetUpSealedItem

class MeetUpDetailFriendPlusViewHolder(
    private val binding: ItemMeetUpDetailFriendPlusBinding,
) : RecyclerView.ViewHolder(binding.root) {
    init {
    }

    fun onBind(data: MeetUpSealedItem.MyGroupPlus) {}
}
