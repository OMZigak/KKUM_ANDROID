package com.teamkkumul.feature.mygroup.mygroupdetail.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.teamkkumul.feature.databinding.ItemMyGroupFriendBinding
import com.teamkkumul.model.MyGroupDetailSealedItem

class MyGroupDetailFriendViewHolder(
    private val binding: ItemMyGroupFriendBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: MyGroupDetailSealedItem.Member) {
        with(binding) {
            ivMyGroupFriendProfileImage.load(data.profileImg)
            tvMyGroupFrinedProfileName.text = data.name
        }
    }
}
