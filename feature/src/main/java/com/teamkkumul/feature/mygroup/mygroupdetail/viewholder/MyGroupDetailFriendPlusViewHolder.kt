package com.teamkkumul.feature.mygroup.mygroupdetail.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.databinding.ItemMyGroupFriendPlusBinding
import com.teamkkumul.model.MyGroupDetailSealedItem

class MyGroupDetailFriendPlusViewHolder(
    private val binding: ItemMyGroupFriendPlusBinding,
    private val onPlusBtnClicked: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            onPlusBtnClicked()
        }
    }

    fun onBind(data: MyGroupDetailSealedItem.MyGroupDetailPlus) {}
}
