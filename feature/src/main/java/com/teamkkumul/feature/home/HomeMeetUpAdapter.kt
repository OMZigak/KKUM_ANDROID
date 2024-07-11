package com.teamkkumul.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemMyGroupRemainMeetUpBinding
import com.teamkkumul.feature.home.viewholder.HomeMeetUpViewHolder
import com.teamkkumul.model.MyGroupMeetUpModel

class HomeMeetUpAdapter(
    private val onMeetUpDetailBtnClicked: () -> Unit,
) : ListAdapter<MyGroupMeetUpModel.Promise, HomeMeetUpViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMeetUpViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMyGroupRemainMeetUpBinding.inflate(inflater, parent, false)
        return HomeMeetUpViewHolder(binding, onMeetUpDetailBtnClicked)
    }

    override fun onBindViewHolder(holder: HomeMeetUpViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        private val diffUtil = ItemDiffCallback<MyGroupMeetUpModel.Promise>(
            onItemsTheSame = { old, new -> old.date == new.date },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
