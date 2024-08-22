package com.teamkkumul.feature.mygroup.mygroupdetail.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.ItemMyGroupRemainMeetUpBinding
import com.teamkkumul.model.MyGroupMeetUpModel

class MyGroupDetailMeetUpViewHolder(
    private val binding: ItemMyGroupRemainMeetUpBinding,
    private val onMeetUpDetailBtnClicked: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var currentItem: MyGroupMeetUpModel.Promise

    init {
        binding.root.setOnClickListener {
            onMeetUpDetailBtnClicked(currentItem.promiseId)
        }
    }

    fun onBind(data: MyGroupMeetUpModel.Promise) = with(binding) {
        currentItem = data
        setDdayTextColor(data)
        tvMeetUpGroupText.visibility = View.GONE
        tvMyGroupRemainMeetUpName.text = data.name
        tvMyGroupRemainMeetUpLocation.text = data.placeName
        tvMyGroupRemainMeetUpTime.text = data.time
    }

    private fun setDdayTextColor(data: MyGroupMeetUpModel.Promise) = with(binding) {
        val colorResId = if (data.dDay == 0) R.color.orange else R.color.gray5
        val color = ContextCompat.getColor(binding.root.context, colorResId)

        tvMyGroupRemainMeetUpDueDateDay.setTextColor(color)
        tvMyGroupRemainMeetUpDueDateDay.text = if (data.dDay == 0) "D-DAY" else "D-${data.dDay}"
    }
}
