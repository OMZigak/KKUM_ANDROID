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
    private val outputSimpleTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val outputSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // a h:mm -> HH:mm:ss
    fun String.changePmAmToTime(): String {
        val time = outputPmAmFormat.parse(this)
        return outputSimpleTimeFormat.format(time)
    }

    // yyyy.MM.dd -> yyyy-mm-dd
    fun String.changeTextToDate(): String {
        val date = outputDateFormat.parse(this)
        return outputSimpleDateFormat.format(date)
    }

    // HH:mm:ss -> a h:mm
    fun String.changeTimeToPmAm(): String {
        val time = outputSimpleTimeFormat.parse(this)
        return outputPmAmFormat.format(time)
    }

    // yyyy-mm-dd -> yyyy.MM.dd
    fun String.changeDateToText(): String {
        val date = outputSimpleDateFormat.parse(this)
        return outputDateFormat.format(date)
    }

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

    fun String.parseTimeOnly(): String {
        val date = inputFormat.parse(this)
        return outputSimpleTimeFormat.format(date)
    }

    fun String.parseDateOnly(): String {
        val date = inputFormat.parse(this)
        return outputSimpleDateFormat.format(date)
    }

    fun String.calculateDday(): Int {
        val eventDate = outputDateFormat.parse(outputDateFormat.format(inputFormat.parse(this)))
        val currentDate =
            outputDateFormat.parse(outputDateFormat.format(System.currentTimeMillis()))

        val diffInMillis = eventDate.time - currentDate.time
        val dDay = -(diffInMillis / (1000 * 60 * 60 * 24)).toInt()

        return dDay
    }
}
