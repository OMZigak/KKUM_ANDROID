package com.teamkkumul.feature.meetup.meetupdetail.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.databinding.ItemMyGroupFriendBinding
import com.teamkkumul.feature.utils.setEmptyImageUrl
import com.teamkkumul.model.MeetUpSealedItem

class MeetUpDetailFriendViewHolder(
    private val binding: ItemMyGroupFriendBinding,

) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: MeetUpSealedItem.Participant) {
        with(binding) {
            ivMyGroupFriendProfileImage.setEmptyImageUrl(data.profileImg)
            tvMyGroupFrinedProfileName.text = data.name
        }
    }
}
