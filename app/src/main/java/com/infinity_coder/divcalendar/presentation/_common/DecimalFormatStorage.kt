package com.infinity_coder.divcalendar.presentation._common

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object DecimalFormatStorage {

    private const val ACCURACY = 2

    val formatter: DecimalFormat

    init {
        val formatSymbols = DecimalFormatSymbols(Locale.getDefault())
        formatSymbols.decimalSeparator = ','
        formatSymbols.groupingSeparator = ' '
        formatter = DecimalFormat("#,###.${"#".repeat(ACCURACY)}", formatSymbols)
    }
}