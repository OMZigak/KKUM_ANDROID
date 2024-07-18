package com.teamkkumul.feature.meetup.lateperson

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemMeetUpCreateFriendBinding
import com.teamkkumul.model.LatePersonModel

class LatePersonAdapter() : ListAdapter<LatePersonModel.LateComers, LatePersonViewHolder>(DiffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): LatePersonViewHolder {
        val binding = ItemMeetUpCreateFriendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return LatePersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LatePersonViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        private val DiffUtil = ItemDiffCallback<LatePersonModel.LateComers>(
            onItemsTheSame = { old, new -> old.participantId == new.participantId },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
