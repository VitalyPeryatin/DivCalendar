package com.infinity_coder.divcalendar.presentation._common

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

@SuppressLint("ConstantLocale")
object DecimalFormatStorage {

    private const val ACCURACY = 2

    val countDecimalFormat by lazy {
        val formatSymbols = DecimalFormatSymbols(Locale.getDefault())
        formatSymbols.groupingSeparator = ' '
        DecimalFormat("#,###", formatSymbols)
    }

    val formatter: DecimalFormat

    init {
        val formatSymbols = DecimalFormatSymbols(Locale.getDefault())
        formatSymbols.decimalSeparator = ','
        formatSymbols.groupingSeparator = ' '
        formatter = DecimalFormat("#,###.${"#".repeat(ACCURACY)}", formatSymbols)
    }
}