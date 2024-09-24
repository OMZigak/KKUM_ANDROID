package com.teamkkumul.feature.mygroup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemMyGroupBinding
import com.teamkkumul.feature.mygroup.viewholder.MyGroupViewHolder
import com.teamkkumul.model.MyGroupModel

class MyGroupAdapter(
    private val onMyGroupListBtnClicked: (Int) -> Unit,
) : ListAdapter<MyGroupModel.Meeting, MyGroupViewHolder>(DiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyGroupViewHolder {
        val binding = ItemMyGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return MyGroupViewHolder(
            binding = binding,
            onMyGroupListBtnClicked = onMyGroupListBtnClicked,
        )
    }

    override fun onBindViewHolder(holder: MyGroupViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        private val DiffUtil = ItemDiffCallback<MyGroupModel.Meeting>(
            onItemsTheSame = { old, new -> old.id == new.id },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
