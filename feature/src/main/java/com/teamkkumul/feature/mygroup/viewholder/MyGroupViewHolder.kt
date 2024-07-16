package com.teamkkumul.feature.mygroup.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.databinding.ItemMyGroupBinding
import com.teamkkumul.model.MyGroupModel

class MyGroupViewHolder(
    private val binding: ItemMyGroupBinding,
    onMyGroupListBtnClicked: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private var item: MyGroupModel.Meeting? = null

    init {
        binding.root.setOnClickListener {
            item?.let {
                onMyGroupListBtnClicked(it.id)
            }
        }
    }

    fun onBind(data: MyGroupModel.Meeting) {
        item = data
        with(binding) {
            tvMyGroupGroupName.text = data.name
            tvMyGroupParticipateInCount.text = data.memberCount.toString()
        }
    }
}
