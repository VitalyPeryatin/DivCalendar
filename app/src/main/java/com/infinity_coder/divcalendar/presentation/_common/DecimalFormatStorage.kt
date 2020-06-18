package com.infinity_coder.divcalendar.presentation._common

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

@SuppressLint("ConstantLocale")
object DecimalFormatStorage {

    private const val ACCURACY = 2

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

    val formatterWithPoints: DecimalFormat by lazy {
        val formatSymbols = DecimalFormatSymbols(Locale.getDefault())
        formatSymbols.groupingSeparator = ' '
        DecimalFormat("#,###.${"#".repeat(ACCURACY)}", formatSymbols)
    }

    val formatterWithoutPoints: DecimalFormat by lazy {
        val formatSymbols = DecimalFormatSymbols(Locale.getDefault())
        formatSymbols.groupingSeparator = ' '
        DecimalFormat("#,###", formatSymbols)
    }
}