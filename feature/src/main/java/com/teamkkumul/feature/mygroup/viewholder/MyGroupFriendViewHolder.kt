package com.teamkkumul.feature.mygroup.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.teamkkumul.feature.databinding.ItemMyGroupFriendBinding
import com.teamkkumul.model.MyGroupSealedItem

class MyGroupFriendViewHolder(
    private val binding: ItemMyGroupFriendBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: MyGroupSealedItem.Member) {
        with(binding) {
            ivMyGroupFriendProfileImage.load(data.profileImg)
            tvMyGroupFrinedProfileName.text = data.name
        }
    }
}
