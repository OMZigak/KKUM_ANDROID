package com.teamkkumul.feature.mygroup.mygroupdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemMyGroupFriendBinding
import com.teamkkumul.feature.databinding.ItemMyGroupFriendPlusBinding
import com.teamkkumul.feature.mygroup.mygroupdetail.viewholder.MyGroupDetailFriendPlusViewHolder
import com.teamkkumul.feature.mygroup.mygroupdetail.viewholder.MyGroupDetailFriendViewHolder
import com.teamkkumul.model.MyGroupSealedItem

class MyGroupDetailFriendAdapter(
    private val onPlusBtnClicked: () -> Unit,
) : ListAdapter<MyGroupSealedItem, RecyclerView.ViewHolder>(DiffUtil) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyGroupDetailFriendViewHolder -> holder.onBind(getItem(position) as MyGroupSealedItem.Member)
            is MyGroupDetailFriendPlusViewHolder -> holder.onBind(getItem(position) as MyGroupSealedItem.MyGroupPlus)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is MyGroupSealedItem.Member -> VIEW_TYPE_MEMBER
            is MyGroupSealedItem.MyGroupPlus -> VIEW_TYPE_PLUS_ICON
            else -> throw IllegalArgumentException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MEMBER -> {
                val binding = ItemMyGroupFriendBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
                MyGroupDetailFriendViewHolder(binding)
            }

            VIEW_TYPE_PLUS_ICON -> {
                val binding = ItemMyGroupFriendPlusBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
                MyGroupDetailFriendPlusViewHolder(binding, onPlusBtnClicked)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    companion object {
        private val DiffUtil = ItemDiffCallback<MyGroupSealedItem>(
            onItemsTheSame = { old, new -> old.javaClass == new.javaClass },
            onContentsTheSame = { old, new -> old == new },
        )
        private const val VIEW_TYPE_MEMBER = 0
        private const val VIEW_TYPE_PLUS_ICON = 1
    }
}
