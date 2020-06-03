package com.infinity_coder.divcalendar.presentation._common.delegate

import android.content.Context
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import com.infinity_coder.divcalendar.data.repositories.SettingsRepository
import com.infinity_coder.divcalendar.presentation._common.DecimalFormatStorage
import java.text.DecimalFormat

object SecurityCurrencyDelegate {

    private val formatterWithPoints: DecimalFormat = DecimalFormatStorage.formatterWithPoints
    private val formatterWithoutPoints: DecimalFormat = DecimalFormatStorage.formatterWithoutPoints
    private val settingsRepository = SettingsRepository

    fun getValueWithCurrency(context: Context, value: Double, currency: String): String {
        val valueStr: String = formatterWithPoints.format(value).toString()
        val currencyBadge = getCurrencyBadge(context, currency)
        return "$valueStr $currencyBadge"
    }

    fun getValueWithCurrencyConsiderCopecks(context: Context, value: Double, currency: String): String {
        val valueStr: String =
            if (settingsRepository.isHideCopecks())
                formatterWithoutPoints.format(value).toString()
            else
                formatterWithPoints.format(value).toString()
        val currencyBadge = getCurrencyBadge(context, currency)
        return "$valueStr $currencyBadge"
    }

    fun getValueWithCurrencyConsiderCopecks(context: Context, value: String, currency: String): String {
        return getValueWithCurrencyConsiderCopecks(context, value.toDouble(), currency)
    }

    fun getCurrencyBadge(context: Context, currency: String): String {
        return when (currency) {
            RateRepository.RUB_RATE -> context.resources.getString(R.string.currency_rub_name)
            RateRepository.USD_RATE -> context.resources.getString(R.string.currency_usd_name)
            else -> throw IllegalStateException("No currency name for such currency: $currency")
        }
    }
}