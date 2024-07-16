package com.teamkkumul.feature.meetupcreate.location

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.databinding.ItemMeetUpCreateLocationBinding
import com.teamkkumul.model.MeetUpCreateLocationModel

class MeetUpCreateLocationViewHolder(
    private val binding: ItemMeetUpCreateLocationBinding,
    private val onMeetUpCreateLocationClicked: (Int) -> Unit,
    private val onMeetUpCreateLocationSelected: (MeetUpCreateLocationModel.Place) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private var item: MeetUpCreateLocationModel.Place? = null

    init {
        binding.root.setOnClickListener {
            item?.let {
                onMeetUpCreateLocationClicked(adapterPosition)
                onMeetUpCreateLocationSelected(it)
            }
        }
    }

    fun onBind(data: MeetUpCreateLocationModel.Place, isSelected: Boolean) {
        item = data
        with(binding) {
            tvMeetUpCreateLocationName.text = data.location
            tvMeetUpCreateRoadNameEnter.text = data.roadAddress
            tvMeetUpCreateRoadNumberEnter.text = data.address
            vMeetUpCreateLocation.isVisible = isSelected
        }
    }
}
