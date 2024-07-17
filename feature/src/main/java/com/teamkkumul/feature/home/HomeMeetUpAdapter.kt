package com.teamkkumul.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemMyGroupRemainMeetUpBinding
import com.teamkkumul.feature.home.viewholder.HomeMeetUpViewHolder
import com.teamkkumul.model.home.HomeTodayMeetingModel

class HomeMeetUpAdapter(
    private val onItemClicked: (Int) -> Unit,
) : ListAdapter<HomeTodayMeetingModel, HomeMeetUpViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMeetUpViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMyGroupRemainMeetUpBinding.inflate(inflater, parent, false)
        return HomeMeetUpViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: HomeMeetUpViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        private val diffUtil = ItemDiffCallback<HomeTodayMeetingModel>(
            onItemsTheSame = { old, new -> old.promiseId == new.promiseId },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
