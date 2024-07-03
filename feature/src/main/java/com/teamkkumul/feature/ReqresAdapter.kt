package com.teamkkumul.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamkkumul.core.ui.view.ItemDiffCallback
import com.teamkkumul.feature.databinding.ItemFriendBinding
import com.teamkkumul.feature.viewholder.ReqresViewHolder
import com.teamkkumul.model.example.ReqresModel

class ReqresAdapter : ListAdapter<ReqresModel, ReqresViewHolder>(reqresDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReqresViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFriendBinding.inflate(inflater, parent, false)
        return ReqresViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReqresViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        private val reqresDiffCallback =
            ItemDiffCallback<ReqresModel>(
                onItemsTheSame = { old, new -> old.id == new.id },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
