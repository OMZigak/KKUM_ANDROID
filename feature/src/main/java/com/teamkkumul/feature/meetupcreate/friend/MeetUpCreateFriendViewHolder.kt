package com.teamkkumul.feature.meetupcreate.friend

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.ItemMeetUpCreateFriendBinding
import com.teamkkumul.model.MeetUpSealedItem

class MeetUpCreateFriendViewHolder(
    private val binding: ItemMeetUpCreateFriendBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val selectedDrawable: Drawable? =
        ContextCompat.getDrawable(binding.root.context, R.drawable.sel_rv_main_selected_item)

    fun onBind(data: MeetUpSealedItem.Participant, isSelected: Boolean) {
        with(binding) {
            ivMeetUpCreateFriendProfileImage.load(data.profileImg)
            tvMyGroupCreateFrinedProfileName.text = data.name
            updateBackground(isSelected)
        }
    }

    private fun updateBackground(isSelected: Boolean) {
        if (isSelected) {
            binding.root.isActivated = true
            selectedDrawable?.let { binding.root.background = it }
        } else {
            binding.root.isActivated = false
            binding.root.background = null
        }
    }
}
