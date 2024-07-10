package com.teamkkumul.feature.mygroup.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.databinding.ItemMyGroupRemainMeetUpBinding
import com.teamkkumul.model.MyGroupMeetUpModel

class MyGroupMeetUpViewHolder(
    private val binding: ItemMyGroupRemainMeetUpBinding,
    private val onMeetUpDetailBtnClicked: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onMeetUpDetailBtnClicked()
        }
    }

    fun onBind(data: MyGroupMeetUpModel.Promise) {
        with(binding) {
            tvMeetUpGroupText.visibility = View.GONE
            tvMyGroupRemainMeetUpDueDateDay.text = data.dDay.toString()
            tvMyGroupRemainMeetUpName.text = data.name
            tvMyGroupRemainMeetUpDate.text = data.date
            tvMyGroupRemainMeetUpLocation.text = data.placeName
            tvMyGroupRemainMeetUpTime.text = data.time
        }
    }
}
