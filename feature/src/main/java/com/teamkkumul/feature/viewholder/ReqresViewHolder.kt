package com.teamkkumul.feature.viewholder

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.teamkkumul.feature.databinding.ItemFriendBinding
import com.teamkkumul.model.example.ReqresModel

class ReqresViewHolder(
    private val binding: ItemFriendBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(reqresData: ReqresModel) = with(binding) {
        ivItemFriendProfile.load(reqresData.avatar)
        tvItemFriendName.text = "${reqresData.lastName} ${reqresData.firstName}"
        tvItemFriendBirthDate.text = reqresData.email
        tvSelfDescription.text = "${reqresData.id}"
        cvBirthViewVisibility()
    }

    private fun cvBirthViewVisibility() = with(binding) {
        cvMelonMusicGreen.isVisible = true
        cvBirthGiftRed.isGone = true
    }
}
