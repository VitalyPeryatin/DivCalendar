package com.infinity_coder.divcalendar.domain._common

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ConstantLocale")
private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

fun convertStingToDate(dateStr: String): Date {
    return dateFormatter.parse(dateStr)!!
}

fun getNowDate(): Date {
    return Calendar.getInstance().apply {
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.HOUR_OF_DAY, 0)
    }.time
}