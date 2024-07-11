package com.teamkkumul.feature.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getCurrentTime(): String {
    val currentTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("a hh:mm", Locale.ENGLISH) // AM/PM 표시, 12시간 형식
    return currentTime.format(formatter)
}
