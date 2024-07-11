package com.teamkkumul.feature.mygroup.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.databinding.ItemMyGroupBinding
import com.teamkkumul.model.MyGroupModel

class MyGroupViewHolder(
    private val binding: ItemMyGroupBinding,
    myGroupListBtnClicked: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: MyGroupModel.Meeting) {
        with(binding) {
            tvMyGroupGroupName.text = data.name
            tvMyGroupParticipateInCount.text = data.memberCount.toString()
        }
    }
}
