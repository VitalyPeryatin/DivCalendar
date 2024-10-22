package com.infinity_coder.divcalendar.presentation._common.delegate

import android.content.Context
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import com.infinity_coder.divcalendar.data.repositories.SettingsRepository
import com.infinity_coder.divcalendar.presentation._common.DecimalFormatStorage
import java.math.BigDecimal
import java.text.DecimalFormat

object SecurityCurrencyDelegate {

    private val formatterWithPoints: DecimalFormat = DecimalFormatStorage.formatterWithPoints
    private val formatterWithoutPoints: DecimalFormat = DecimalFormatStorage.formatterWithoutPoints

    private val settingsRepository = SettingsRepository

    fun getValueWithCurrency(context: Context, value: BigDecimal, currency: String): String {
        val valueStr: String = formatterWithPoints.format(value).toString()
        val currencyBadge = getCurrencyBadge(context, currency)
        return "$valueStr $currencyBadge"
    }

    fun getValueWithCurrencyConsiderCopecks(context: Context, value: BigDecimal, currency: String): String {
        val valueStr = getValueConsiderCopecks(value)
        val currencyBadge = getCurrencyBadge(context, currency)
        return "$valueStr $currencyBadge"
    }

    fun getValueConsiderCopecks(value: String): String {
        return getValueConsiderCopecks(value.toBigDecimal())
    }

    fun getValueConsiderCopecks(value: BigDecimal): String {
        return if (settingsRepository.isHideCopecks())
            formatterWithoutPoints.format(value.toBigInteger()).toString()
        else
            formatterWithPoints.format(value).toString()
    }

    fun getCurrencyBadge(context: Context, currency: String): String {
        return when (currency) {
            RateRepository.RUB_RATE -> context.resources.getString(R.string.currency_rub_name)
            RateRepository.USD_RATE -> context.resources.getString(R.string.currency_usd_name)
            else -> throw IllegalStateException("No currency name for such currency: $currency")
        }
    }
}