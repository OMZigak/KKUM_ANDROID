package com.teamkkumul.feature.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

fun getCurrentTime(): String {
    val currentTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("a hh:mm", Locale.ENGLISH) // AM/PM 표시, 12시간 형식
    return currentTime.format(formatter)
}

fun calculateReadyStartTime(
    promiseTime: String?,
    preparationTime: Int?,
    movingTime: Int?,
    extraTime: Int = 10,
): String {
    if (promiseTime == null || preparationTime == null || movingTime == null) return ""

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = dateFormat.parse(promiseTime)
    val calendar = Calendar.getInstance()
    calendar.time = date

    // 약속 시간에서 준비 시간, 이동 시간, 여유 시간 10분을 뺌
    calendar.add(Calendar.MINUTE, -preparationTime)
    calendar.add(Calendar.MINUTE, -movingTime)
    calendar.add(Calendar.MINUTE, -extraTime)

    return String.format(
        "%02d시 %02d분",
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
    )
}

fun parseMinutesToHoursAndMinutes(minutes: Int?): String {
    minutes ?: return ""
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return if (hours > 0) {
        String.format("%d시간 %02d분", hours, remainingMinutes)
    } else {
        String.format("%d분", remainingMinutes)
    }
}
