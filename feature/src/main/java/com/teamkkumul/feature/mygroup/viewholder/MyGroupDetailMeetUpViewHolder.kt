package com.teamkkumul.feature.mygroup.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.ItemMyGroupRemainMeetUpBinding
import com.teamkkumul.model.MyGroupMeetUpModel

class MyGroupDetailMeetUpViewHolder(
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
        setDdayTextColor(data)
    }

    private fun setDdayTextColor(data: MyGroupMeetUpModel.Promise) = with(binding) {
        if (data.dDay == 0) {
            val orangeColor = ContextCompat.getColor(binding.root.context, R.color.orange)
            tvMyGroupRemainMeetUpDueDateDay.setTextColor(orangeColor)
        } else {
            val defaultColor = ContextCompat.getColor(binding.root.context, R.color.gray8)
            tvMyGroupRemainMeetUpDueDateDay.setTextColor(defaultColor)
        }
    }
}
