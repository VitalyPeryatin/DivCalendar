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
}