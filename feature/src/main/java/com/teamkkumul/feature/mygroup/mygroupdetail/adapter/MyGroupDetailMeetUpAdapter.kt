package com.teamkkumul.feature.mygroup.mygroupdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemMyGroupRemainMeetUpBinding
import com.teamkkumul.feature.mygroup.mygroupdetail.viewholder.MyGroupDetailMeetUpViewHolder
import com.teamkkumul.model.MyGroupMeetUpModel

class MyGroupDetailMeetUpAdapter(
    private val onMeetUpDetailBtnClicked: () -> Unit,
) : ListAdapter<MyGroupMeetUpModel.Promise, MyGroupDetailMeetUpViewHolder>(DiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyGroupDetailMeetUpViewHolder {
        val binding = ItemMyGroupRemainMeetUpBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return MyGroupDetailMeetUpViewHolder(binding, onMeetUpDetailBtnClicked)
    }

    override fun onBindViewHolder(holder: MyGroupDetailMeetUpViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        private val DiffUtil = ItemDiffCallback<MyGroupMeetUpModel.Promise>(
            onItemsTheSame = { old, new -> old.date == new.date },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
