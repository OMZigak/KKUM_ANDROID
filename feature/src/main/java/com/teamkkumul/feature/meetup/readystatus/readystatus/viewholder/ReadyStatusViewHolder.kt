package com.teamkkumul.feature.meetup.readystatus.readystatus.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.databinding.ItemReadyStatusBinding
import com.teamkkumul.model.home.HomeMembersStatus

class ReadyStatusViewHolder(
    private val binding: ItemReadyStatusBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: HomeMembersStatus.Participant

    fun onBind(data: HomeMembersStatus.Participant) = with(binding) {
        item = data
        participant = data
        executePendingBindings()
    }
}
