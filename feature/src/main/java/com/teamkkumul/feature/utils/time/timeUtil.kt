package com.teamkkumul.feature.utils.time

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("ConstantLocale")
object TimeUtils {
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val outputPmAmFormat = SimpleDateFormat("a h:mm", Locale.US)
    private val outputDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    private val outputDateFormatText = SimpleDateFormat("MM월 dd일", Locale.getDefault())

    fun String.formatTimeToPmAm(): String {
        val date = inputFormat.parse(this)
        return outputPmAmFormat.format(date)
    }

    fun String.parseDateToYearMonthDay(): String {
        val date = inputFormat.parse(this)
        return outputDateFormat.format(date)
    }

    fun String.parseDateToMonthDay(): String {
        val date = inputFormat.parse(this)
        return outputDateFormatText.format(date)
    }

    fun String.calculateDday(): String {
        val eventDate = outputDateFormat.parse(outputDateFormat.format(inputFormat.parse(this)))
        val currentDate = outputDateFormat.parse(outputDateFormat.format(System.currentTimeMillis()))

        val diffInMillis = eventDate.time - currentDate.time
        val dDay = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()

        return when {
            dDay > 0 -> "D-$dDay"
            dDay == 0 -> "D-DAY"
            else -> "D+${-dDay}"
        }
    }
}
