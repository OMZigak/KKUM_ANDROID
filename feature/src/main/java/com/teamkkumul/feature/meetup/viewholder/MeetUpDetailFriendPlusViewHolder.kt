package com.teamkkumul.feature.meetup.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.databinding.ItemMyGroupFriendPlusBinding
import com.teamkkumul.model.MeetUpSealedItem

class MeetUpDetailFriendPlusViewHolder(
    private val binding: ItemMyGroupFriendPlusBinding,
) : RecyclerView.ViewHolder(binding.root) {
    init {
    }

    fun onBind(data: MeetUpSealedItem.MyGroupPlus) {}
}
