package com.infinity_coder.divcalendar.presentation._common

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    fun formatDate(dateStr: String, months: Array<String>): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = simpleDateFormat.parse(dateStr) ?: Date()
        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = months[calendar.get(Calendar.MONTH)]
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return "$day $month $year"
    }
}