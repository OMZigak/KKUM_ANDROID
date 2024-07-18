package com.teamkkumul.feature.meetup.lateperson

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.teamkkumul.feature.databinding.ItemMeetUpCreateFriendBinding
import com.teamkkumul.model.LatePersonModel

class LatePersonViewHolder(
    private val binding: ItemMeetUpCreateFriendBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(latePerson: LatePersonModel?) {
        with(binding) {
            ivMeetUpCreateFriendProfileImage.load(latePerson.profileImg)
            tvMyGroupCreateFrinedProfileName.text = latePerson.name
        }
    }
}
