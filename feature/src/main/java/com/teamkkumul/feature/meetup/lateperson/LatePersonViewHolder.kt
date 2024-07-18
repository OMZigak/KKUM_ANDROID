package com.teamkkumul.feature.meetup.lateperson

import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.databinding.ItemMeetUpCreateFriendBinding
import com.teamkkumul.feature.utils.setEmptyImageUrl
import com.teamkkumul.model.LatePersonModel

class LatePersonViewHolder(
    private val binding: ItemMeetUpCreateFriendBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(latePerson: LatePersonModel.LateComers?) {
        with(binding) {
            ivMeetUpCreateFriendProfileImage.setEmptyImageUrl(latePerson?.profileImg)
            tvMyGroupCreateFrinedProfileName.text = latePerson?.name
        }
    }
}
