package com.teamkkumul.feature.meetup.meetupdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemMyGroupFriendBinding
import com.teamkkumul.feature.meetup.meetupdetail.viewholder.MeetUpDetailFriendViewHolder
import com.teamkkumul.model.MeetUpParticipantItem

class MeetUpDetailListAdapter() :
    ListAdapter<MeetUpParticipantItem, RecyclerView.ViewHolder>(DiffUtil) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MeetUpDetailFriendViewHolder -> holder.onBind(getItem(position) as MeetUpParticipantItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is MeetUpParticipantItem -> VIEW_TYPE_MEMBER
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

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    companion object {
        private val DiffUtil = ItemDiffCallback<MeetUpParticipantItem>(
            onItemsTheSame = { old, new -> old.javaClass == new.javaClass },
            onContentsTheSame = { old, new -> old == new },
        )
        private const val VIEW_TYPE_MEMBER = 0
    }
}
