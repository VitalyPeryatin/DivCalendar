package com.infinity_coder.divcalendar.domain._common

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    @SuppressLint("ConstantLocale")
    val basicDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @SuppressLint("ConstantLocale")
    val postDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    fun formatDate(dateStr: String, months: Array<String>): String {
        val date = basicDateFormat.parse(dateStr) ?: Date()
        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = months[calendar.get(Calendar.MONTH)]
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return "$day $month $year"
    }

    fun formatDateForPosts(dateStr: String, months: Array<String>): String {
        val date = postDateFormat.parse(dateStr) ?: Date()
        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = months[calendar.get(Calendar.MONTH)]
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY).addZerosInFrontIfNotDozen()
        val minute = calendar.get(Calendar.MINUTE).addZerosInFrontIfNotDozen()

        return "$day $month $year, $hour:$minute"
    }

    private fun Int.addZerosInFrontIfNotDozen(): String {
        if (this.toString().length > 1)
            return this.toString()

        return "0$this"
    }

    fun getCurrentYear(): String {
        return Calendar.getInstance().get(Calendar.YEAR).toString()
    }
}