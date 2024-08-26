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

