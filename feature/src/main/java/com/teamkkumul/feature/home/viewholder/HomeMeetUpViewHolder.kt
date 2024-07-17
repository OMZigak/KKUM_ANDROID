package com.teamkkumul.feature.home.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.ItemMyGroupRemainMeetUpBinding
import com.teamkkumul.model.home.HomeTodayMeetingModel

class HomeMeetUpViewHolder(
    private val binding: ItemMyGroupRemainMeetUpBinding,
    private val onItemClicked: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private var item: HomeTodayMeetingModel? = null

    init {
        binding.root.setOnClickListener {
            item?.let { onItemClicked(it.promiseId) }
        }
    }

    fun onBind(data: HomeTodayMeetingModel) = with(binding) {
        item = data
        tvMeetUpGroupText.visibility = View.VISIBLE
        setDdayTextColor(data)
        tvMyGroupRemainMeetUpName.text = data.name
        tvMyGroupRemainMeetUpDate.text = data.date
        tvMyGroupRemainMeetUpLocation.text = data.placeName
        tvMyGroupRemainMeetUpTime.text = data.time
    }

    private fun setDdayTextColor(data: HomeTodayMeetingModel) = with(binding) {
        val colorResId = if (data.dDay == 0) R.color.orange else R.color.gray5
        val color = ContextCompat.getColor(binding.root.context, colorResId)

        tvMyGroupRemainMeetUpDueDateDay.setTextColor(color)
        tvMyGroupRemainMeetUpDueDateDay.text = if (data.dDay == 0) "D-DAY" else "D-${data.dDay}"
    }
}
