package com.infinity_coder.divcalendar.presentation._common

import android.content.Context
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import java.text.DecimalFormat

object SecurityCurrencyDelegate {

    fun getValueWithCurrency(context: Context, value: Float, currency: String, accuracy: Int = 2): String {
        val formatter = DecimalFormat("0.${"#".repeat(accuracy)}")
        val valueStr = formatter.format(value)
        val currencyBadge = getCurrencyBadge(context, currency)
        return "$valueStr $currencyBadge"
    }

    fun getValueWithCurrency(context: Context, value: String, currency: String, accuracy: Int = 2): String {
        return getValueWithCurrency(context, value.toFloat(), currency, accuracy)
    }

    private fun getCurrencyBadge(context: Context, currency: String): String {
        return when (currency) {
            RateRepository.RUB_RATE -> context.resources.getString(R.string.currency_rub_name)
            RateRepository.USD_RATE -> context.resources.getString(R.string.currency_usd_name)
            else -> throw IllegalStateException("No currency name for such currency: $currency")
        }
    }
}