package com.infinity_coder.divcalendar.presentation._common

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

@SuppressLint("ConstantLocale")
object DecimalFormatStorage {

    private const val ACCURACY = 2
    const val EPS_ACCURACY = 0.0000001

    val countEditTextDecimalFormat by lazy {
        val formatSymbols = DecimalFormatSymbols(Locale.getDefault())
        formatSymbols.groupingSeparator = ' '
        DecimalFormat("#,###", formatSymbols)
    }

    val priceEditTextDecimalFormat by lazy {
        val formatSymbols = DecimalFormatSymbols(Locale.getDefault())
        formatSymbols.groupingSeparator = ' '
        DecimalFormat("#,###", formatSymbols)
    }

    val formatter: DecimalFormat by lazy {
        val formatSymbols = DecimalFormatSymbols(Locale.getDefault())
        formatSymbols.groupingSeparator = ' '
        DecimalFormat("#,###.${"#".repeat(ACCURACY)}", formatSymbols)
    }

    val formatterWithoutPoints: DecimalFormat by lazy {
        val formatSymbols = DecimalFormatSymbols(Locale.getDefault())
        formatSymbols.groupingSeparator = ' '
        DecimalFormat("#", formatSymbols)
    }
}