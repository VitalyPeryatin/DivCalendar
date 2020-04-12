package com.infinity_coder.divcalendar.domain._common

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    val basicDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun formatDate(dateStr: String, months: Array<String>): String {
        val date = basicDateFormat.parse(dateStr) ?: Date()
        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = months[calendar.get(Calendar.MONTH)]
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return "$day $month $year"
    }

    fun getCurrentYear():String{
        return Calendar.getInstance().get(Calendar.YEAR).toString()
    }
}