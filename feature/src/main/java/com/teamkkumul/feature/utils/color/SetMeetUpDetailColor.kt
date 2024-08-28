package com.teamkkumul.feature.utils.color

import com.teamkkumul.feature.R

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
