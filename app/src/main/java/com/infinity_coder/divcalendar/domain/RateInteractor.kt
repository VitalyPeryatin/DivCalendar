package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.repositories.RateRepository

class RateInteractor {
    suspend fun getUsdToRubRate(): Float {
        return RateRepository.getRates().usdToRub
    }

    suspend fun getRubToUsdRate(): Float {
        return RateRepository.getRates().rubToUsd
    }

    fun getDisplayCurrency(): String {
        return RateRepository.getDisplayCurrency()
    }

    fun saveDisplayCurrency(currency: String) {
        RateRepository.saveDisplayCurrency(currency)
    }

    suspend fun convertCurrencies(value: Float, from: String, to: String): Float {
        return when {
            from == RateRepository.USD_RATE && to == RateRepository.RUB_RATE -> {
                value / getUsdToRubRate()
            }
            from == RateRepository.RUB_RATE && to == RateRepository.USD_RATE -> {
                value / getRubToUsdRate()
            }
            else -> value
        }
    }
}