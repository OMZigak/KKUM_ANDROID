package com.teamkkumul.feature.meetupcreate.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemMeetUpCreateLocationBinding
import com.teamkkumul.model.MeetUpCreateLocationModel

class MeetUpCreateLocationAdapter(
    private val onMeetUpCreateLocationSelected: (MeetUpCreateLocationModel.Place) -> Unit,
) :
    ListAdapter<MeetUpCreateLocationModel.Place, MeetUpCreateLocationViewHolder>(DiffUtil) {
    private var selectedPosition = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MeetUpCreateLocationViewHolder {
        val binding = ItemMeetUpCreateLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return MeetUpCreateLocationViewHolder(
            binding = binding,
            onMeetUpCreateLocationClicked = { position ->
                val previousPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousPosition)
                notifyItemChanged(position)
            },
            onMeetUpCreateLocationSelected = onMeetUpCreateLocationSelected,
        )
    }

    override fun onBindViewHolder(holder: MeetUpCreateLocationViewHolder, position: Int) {
        holder.onBind(getItem(position), position == selectedPosition)
    }

    companion object {
        private val DiffUtil = ItemDiffCallback<MeetUpCreateLocationModel.Place>(
            onItemsTheSame = { old, new -> old.address == new.address },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
