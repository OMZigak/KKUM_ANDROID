package com.teamkkumul.feature.meetup.readystatus.readystatus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemReadyStatusBinding
import com.teamkkumul.feature.meetup.readystatus.readystatus.viewholder.ReadyStatusViewHolder
import com.teamkkumul.model.home.HomeMembersStatus

class ReadyStatusAdapter :
    ListAdapter<HomeMembersStatus.Participant, ReadyStatusViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadyStatusViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReadyStatusBinding.inflate(inflater, parent, false)
        return ReadyStatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReadyStatusViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        private val diffUtil = ItemDiffCallback<HomeMembersStatus.Participant>(
            onItemsTheSame = { old, new -> old.memberId == new.memberId },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
