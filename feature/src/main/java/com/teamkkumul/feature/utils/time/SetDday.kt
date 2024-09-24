package com.teamkkumul.feature.utils.time

import com.teamkkumul.feature.R

fun setDday(dDay: Int): String = when {
    dDay == 0 -> "D-DAY"
    dDay > 0 -> "D+$dDay"
    else -> "D$dDay"
}

fun setDdayTextColor(dDay: Int): Int = when {
    dDay == 0 -> R.color.orange
    dDay > 0 -> R.color.gray3
    else -> R.color.gray5
}

fun setPromiseTextColor(dDay: Int): Int = when {
    dDay > 0 -> R.color.gray3
    else -> R.color.gray8
}

fun setMeetUpDetailTextColor(dDay: Int): Int = when {
    dDay <= 0 -> R.color.main_color
    else -> R.color.gray4
}

fun setMeetUpTitleColor(dDay: Int): Int = when {
    dDay <= 0 -> R.color.gray7
    else -> R.color.gray4
}

fun setMeetUpDetailImage(dDay: Int): Int = when {
    dDay <= 0 -> R.drawable.ic_meet_up_detail_receipt
    else -> R.drawable.ic_meet_up_detail_receipt_gray
}
