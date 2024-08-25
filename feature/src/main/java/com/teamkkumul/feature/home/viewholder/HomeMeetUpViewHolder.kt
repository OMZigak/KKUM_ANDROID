package com.teamkkumul.feature.home.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.ItemMyGroupRemainMeetUpBinding
import com.teamkkumul.feature.utils.time.TimeUtils.formatTimeToPmAm
import com.teamkkumul.feature.utils.time.TimeUtils.parseDateToYearMonthDay
import com.teamkkumul.feature.utils.time.setDday
import com.teamkkumul.model.home.HomeTodayMeetingModel

class HomeMeetUpViewHolder(
    private val binding: ItemMyGroupRemainMeetUpBinding,
    private val onItemClicked: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: HomeTodayMeetingModel

    init {
        binding.root.setOnClickListener {
            onItemClicked(item.promiseId)
        }
    }

    fun onBind(data: HomeTodayMeetingModel) = with(binding) {
        item = data
        setDdayTextColor(data)
        tvMeetUpGroupText.visibility = View.VISIBLE
        tvMeetUpGroupText.text = data.meetingName
        tvMyGroupRemainMeetUpName.text = data.name
        tvMyGroupRemainMeetUpDate.text = data.time.parseDateToYearMonthDay()
        tvMyGroupRemainMeetUpLocation.text = data.placeName
        tvMyGroupRemainMeetUpTime.text = data.time.formatTimeToPmAm()
    }

    private fun setDdayTextColor(data: HomeTodayMeetingModel) = with(binding) {
        val colorResId = if (data.dDay == 0) R.color.orange else R.color.gray5
        val color = ContextCompat.getColor(binding.root.context, colorResId)

        tvMyGroupRemainMeetUpDueDateDay.setTextColor(color)
        tvMyGroupRemainMeetUpDueDateDay.text = setDday(data.dDay)
    }
}
