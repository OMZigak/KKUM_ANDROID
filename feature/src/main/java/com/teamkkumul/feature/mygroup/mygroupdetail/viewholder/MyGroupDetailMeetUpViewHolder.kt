package com.teamkkumul.feature.mygroup.mygroupdetail.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamkkumul.core.ui.view.colorOf
import com.teamkkumul.feature.databinding.ItemMyGroupRemainMeetUpBinding
import com.teamkkumul.feature.utils.time.TimeUtils.formatTimeToPmAm
import com.teamkkumul.feature.utils.time.TimeUtils.parseDateToYearMonthDay
import com.teamkkumul.feature.utils.time.setDday
import com.teamkkumul.feature.utils.time.setDdayTextColor
import com.teamkkumul.feature.utils.time.setPromiseTextColor
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
        setTextColor(data.dDay)
        tvMeetUpGroupText.visibility = View.GONE
        tvMyGroupRemainMeetUpDueDateDay.text = setDday(data.dDay)
        tvMyGroupRemainMeetUpName.text = data.name
        tvMyGroupRemainMeetUpLocation.text = data.placeName
        tvMyGroupRemainMeetUpTime.text = data.time.formatTimeToPmAm()
        tvMyGroupRemainMeetUpDate.text = data.time.parseDateToYearMonthDay()
    }

    private fun setTextColor(dDay: Int) = with(binding) {
        val promiseColor = colorOf(setPromiseTextColor(dDay))
        tvMyGroupRemainMeetUpDueDateDay.setTextColor(colorOf(setDdayTextColor(dDay)))
        tvMyGroupRemainMeetUpName.setTextColor(promiseColor)
        tvMyGroupRemainMeetUpLocation.setTextColor(promiseColor)
        tvMyGroupRemainMeetUpTime.setTextColor(promiseColor)
        tvMyGroupRemainMeetUpDate.setTextColor(promiseColor)
    }
}
