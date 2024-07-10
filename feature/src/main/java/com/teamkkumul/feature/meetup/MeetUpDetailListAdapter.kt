package com.teamkkumul.feature.mygroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemMyGroupFriendBinding
import com.teamkkumul.feature.databinding.ItemMyGroupFriendPlusBinding
import com.teamkkumul.feature.meetup.viewholder.MeetUpDetailFriendPlusViewHolder
import com.teamkkumul.feature.meetup.viewholder.MeetUpDetailFriendViewHolder
import com.teamkkumul.model.MeetUpSealedItem

class MeetUpDetailListAdapter() :
    ListAdapter<MeetUpSealedItem, RecyclerView.ViewHolder>(DiffUtil) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MeetUpDetailFriendViewHolder -> holder.onBind(getItem(position) as MeetUpSealedItem.Participant)
            is MeetUpDetailFriendPlusViewHolder -> holder.onBind(getItem(position) as MeetUpSealedItem.MyGroupPlus)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is MeetUpSealedItem.Participant -> VIEW_TYPE_MEMBER
            is MeetUpSealedItem.MyGroupPlus -> VIEW_TYPE_PLUS_ICON
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
                MeetUpDetailFriendViewHolder(binding)
            }

            VIEW_TYPE_PLUS_ICON -> {
                val binding = ItemMyGroupFriendPlusBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
                MeetUpDetailFriendPlusViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    companion object {
        private val DiffUtil = ItemDiffCallback<MeetUpSealedItem>(
            onItemsTheSame = { old, new -> old.javaClass == new.javaClass },
            onContentsTheSame = { old, new -> old == new },
        )
        private const val VIEW_TYPE_MEMBER = 0
        private const val VIEW_TYPE_PLUS_ICON = 1
    }
}
