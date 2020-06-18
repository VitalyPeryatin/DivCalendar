package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.repositories.RateRepository
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class RateInteractor {
    private fun getUsdToRubRate(): BigDecimal {
        return BigDecimal(RateRepository.getRates().usdToRub.toString())
    }

    private fun getRubToUsdRate(): BigDecimal {
        return BigDecimal(RateRepository.getRates().rubToUsd.toString())
    }

    fun getDisplayCurrency(): String {
        return RateRepository.getDisplayCurrency()
    }

    fun saveDisplayCurrency(currency: String) {
        RateRepository.saveDisplayCurrency(currency)
    }

    fun convertCurrencies(value: BigDecimal, from: String, to: String): BigDecimal {
        return when {
            from == RateRepository.USD_RATE && to == RateRepository.RUB_RATE -> {
                value.divide(getUsdToRubRate(), MathContext(2, RoundingMode.HALF_EVEN))
            }
            from == RateRepository.RUB_RATE && to == RateRepository.USD_RATE -> {
                value.divide(getRubToUsdRate(), MathContext(2, RoundingMode.HALF_EVEN))
            }
            else -> value
        }
    }
}