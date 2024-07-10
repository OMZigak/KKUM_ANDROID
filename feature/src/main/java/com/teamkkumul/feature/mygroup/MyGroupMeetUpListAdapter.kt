package com.teamkkumul.feature.mygroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemMyGroupRemainMeetUpBinding
import com.teamkkumul.feature.mygroup.viewholder.MyGroupMeetUpViewHolder
import com.teamkkumul.model.MyGroupMeetUpModel

class MyGroupMeetUpListAdapter(
    private val onMeetUpDetailBtnClicked: () -> Unit,
) : ListAdapter<MyGroupMeetUpModel.Promise, RecyclerView.ViewHolder>(DiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupMeetUpViewHolder {
        val binding = ItemMyGroupRemainMeetUpBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return MyGroupMeetUpViewHolder(binding, onMeetUpDetailBtnClicked)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyGroupMeetUpViewHolder -> holder.onBind(getItem(position))
        }
    }

    companion object {
        private val DiffUtil = ItemDiffCallback<MyGroupMeetUpModel.Promise>(
            onItemsTheSame = { old, new -> old.date == new.date },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
