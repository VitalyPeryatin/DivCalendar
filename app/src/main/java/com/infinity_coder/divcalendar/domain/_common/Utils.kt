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

fun getNowStringDate(): String {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.HOUR_OF_DAY, 0)
    }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH).run {
        val monthStr = (this + 1).toString()
        if (monthStr.length == 1) "0$monthStr" else monthStr
    }
    val day = calendar.get(Calendar.DAY_OF_MONTH).run {
        val dayStr = this.toString()
        if (dayStr.length == 1) "0$this" else dayStr
    }
    return "$year-$month-$day"
}

fun isExpiredDate(dateStr: String): Boolean {
    val paymentDate = convertStingToDate(dateStr)
    return paymentDate.before(getNowDate())
}