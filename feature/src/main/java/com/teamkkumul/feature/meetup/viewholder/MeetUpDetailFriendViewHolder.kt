package com.teamkkumul.feature.meetup.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.teamkkumul.feature.databinding.ItemMeetUpDetailFriendBinding
import com.teamkkumul.model.MeetUpSealedItem

class MeetUpDetailFriendViewHolder(
    private val binding: ItemMeetUpDetailFriendBinding,

) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: MeetUpSealedItem.Participant) {
        with(binding) {
            ivMeetUpDetailFriendProfileImage.load(data.profileImg)
            tvMeetUpDetailProfileName.text = data.name
        }
    }
}
