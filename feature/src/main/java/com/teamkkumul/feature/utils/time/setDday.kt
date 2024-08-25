package com.teamkkumul.feature.utils.time

fun setDday(dDay: Int): String = when {
    dDay == 0 -> "D-DAY"
    dDay > 0 -> "D+$dDay"
    else -> "D$dDay"
}
