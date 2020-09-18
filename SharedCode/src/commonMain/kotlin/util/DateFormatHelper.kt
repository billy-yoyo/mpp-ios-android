package com.jetbrains.handson.mpp.mobile.util

import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.parse

object DateFormatHelper {
    private const val inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    private const val outputDateTimeFormat = "MMM d, h:mm a"
    private const val outputDateFormat = "MMM d"
    private const val outputTimeFormat = "h:mm a"

    private val inputFormatter = DateFormat(inputFormat)
    private val outputDateTimeFormatter = DateFormat(outputDateTimeFormat)
    private val outputDateFormatter = DateFormat(outputDateFormat)
    private val outputTimeFormatter = DateFormat(outputTimeFormat)

    private fun format(datetime: String, formatter: DateFormat): String {
        return try {
            val date = inputFormatter.parse(datetime)
            formatter.format(date)
        } catch (e: Throwable) {
            datetime
        }
    }

    fun formatDateTime(datetime: String): String = format(datetime, outputDateTimeFormatter)
    fun formatTime(datetime: String): String = format(datetime, outputTimeFormatter)
    fun formatDate(datetime: String): String = format(datetime, outputDateFormatter)

    fun formatForInput(date: DateTimeTz): String {
        return date.format(inputFormatter)
    }
}