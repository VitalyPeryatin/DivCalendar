package com.infinity_coder.divcalendar.presentation._common

import android.content.Context
import android.util.Log
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import java.util.*

object SecurityCurrencyDelegate {

    fun getCurrencyBadge(context: Context, currency: String): String {
        return when (currency) {
            RateRepository.RUB_RATE -> context.resources.getString(R.string.currency_rub_name)
            RateRepository.USD_RATE -> context.resources.getString(R.string.currency_usd_name)
            else -> throw IllegalStateException("No currency name for such currency: $currency")
        }
    }

    fun getValueWithCurrency(context: Context, value: Float, currency: String, accuracy: Int = 2): String {
        val valueStr = String.format(Locale.getDefault(), "%.${accuracy}f", value)
        Log.d("SecCurrencyDelegate", valueStr)
        val newValueStr = deleteLastZeros(valueStr)
        val currencyBadge = getCurrencyBadge(context, currency)
        return "$newValueStr $currencyBadge"
    }

    fun getValueWithCurrency(context: Context, value: Double, currency: String, accuracy: Int = 2): String {
        val valueStr = String.format(Locale.getDefault(), "%.${accuracy}f", value)
        Log.d("SecCurrencyDelegate", valueStr)
        val newValueStr = deleteLastZeros(valueStr)
        val currencyBadge = getCurrencyBadge(context, currency)
        return "$newValueStr $currencyBadge"
    }

    private fun deleteLastZeros(valueStr: String): String {
        val wholePart: String
        val fractionPart: String
        valueStr.split(",").run {
            wholePart = this[0]
            fractionPart = this[1]
        }

        var newFractionPart = ""

        for (i in (fractionPart.length - 1) downTo 0) {
            val number = fractionPart[i]

            if (number != '0' || newFractionPart.isNotEmpty()) {
                newFractionPart = number + newFractionPart
            }
        }

        return if (newFractionPart.isNotEmpty()) {
            "$wholePart,$newFractionPart"
        } else {
            wholePart
        }
    }
}