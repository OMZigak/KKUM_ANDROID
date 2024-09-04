package com.teamkkumul.feature.home.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.core.ui.view.colorOf
import com.teamkkumul.feature.databinding.ItemMyGroupRemainMeetUpBinding
import com.teamkkumul.feature.utils.time.TimeUtils.formatTimeToPmAm
import com.teamkkumul.feature.utils.time.TimeUtils.parseDateToYearMonthDay
import com.teamkkumul.feature.utils.time.setDday
import com.teamkkumul.feature.utils.time.setDdayTextColor
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
        tvMyGroupRemainMeetUpDueDateDay.setTextColor(colorOf(setDdayTextColor(data.dDay)))
        tvMyGroupRemainMeetUpDueDateDay.text = setDday(data.dDay)
        tvMeetUpGroupText.visibility = View.VISIBLE
        tvMeetUpGroupText.text = data.name
        tvMyGroupRemainMeetUpName.text = data.meetingName
        tvMyGroupRemainMeetUpDate.text = data.time.parseDateToYearMonthDay()
        tvMyGroupRemainMeetUpLocation.text = data.placeName
        tvMyGroupRemainMeetUpTime.text = data.time.formatTimeToPmAm()
    }
}
