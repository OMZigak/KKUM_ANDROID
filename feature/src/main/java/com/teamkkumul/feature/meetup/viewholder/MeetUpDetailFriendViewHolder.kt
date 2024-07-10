package com.teamkkumul.feature.meetup.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.teamkkumul.feature.databinding.ItemMyGroupFriendBinding
import com.teamkkumul.model.MeetUpSealedItem

class MeetUpDetailFriendViewHolder(
    private val binding: ItemMyGroupFriendBinding,

) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: MeetUpSealedItem.Participant) {
        with(binding) {
            ivMyGroupFriendProfileImage.load(data.profileImg)
            tvMyGroupFrinedProfileName.text = data.name
        }
    }
}
