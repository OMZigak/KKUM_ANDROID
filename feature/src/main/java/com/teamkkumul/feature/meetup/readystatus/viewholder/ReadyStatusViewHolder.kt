package com.teamkkumul.feature.meetup.readystatus.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.databinding.ItemReadyStatusBinding
import com.teamkkumul.model.MeetUpDetailFriendModel

class ReadyStatusViewHolder(
    private val binding: ItemReadyStatusBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private var item: MeetUpDetailFriendModel.Participant? = null
    fun onBind(data: MeetUpDetailFriendModel.Participant) = with(binding) {
        item = data
        participant = data
        executePendingBindings()
    }
}
