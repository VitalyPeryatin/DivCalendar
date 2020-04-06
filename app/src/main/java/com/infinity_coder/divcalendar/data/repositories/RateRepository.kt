package com.infinity_coder.divcalendar.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.infinity_coder.divcalendar.data.network.model.CurrencyRateNetworkModel
import com.infinity_coder.divcalendar.presentation.App

object RateRepository {

    private const val CURRENCY_PREF_NAME = "Currency"
    private const val RUB_TO_USD_KEY = "rub_to_usd"
    private const val USD_TO_RUB_KEY = "usd_to_rub"
    private const val DISPLAY_CURRENCY_KEY = "display_currency"

    const val RUB_RATE = "RUB"
    const val USD_RATE = "USD"

    private val currencyPreferences = App.instance.applicationContext.getSharedPreferences(
        CURRENCY_PREF_NAME,
        Context.MODE_PRIVATE
    )

    suspend fun getRates(): CurrencyRateNetworkModel {
        return try {
            getRatesFromNetworkAndSaveToDB()
        } catch (e: Exception) {
            getRatesFromPref()
        }
    }

    private fun getRatesFromPref(): CurrencyRateNetworkModel {
        return CurrencyRateNetworkModel(
            rubToUsd = currencyPreferences.getFloat(RUB_TO_USD_KEY, 0f),
            usdToRub = currencyPreferences.getFloat(USD_TO_RUB_KEY, 0f)
        )
    }

    private suspend fun getRatesFromNetworkAndSaveToDB(): CurrencyRateNetworkModel {
        val dbPosts = getRatesFromNetwork()
        saveRatesToPref(dbPosts)
        return dbPosts
    }

    private suspend fun saveRatesToPref(rates: CurrencyRateNetworkModel) {
        currencyPreferences.edit {
            putFloat(RUB_TO_USD_KEY, rates.rubToUsd)
            putFloat(USD_TO_RUB_KEY, rates.usdToRub)
        }
    }

    private suspend fun getRatesFromNetwork(): CurrencyRateNetworkModel {
        return CurrencyRateNetworkModel(
            rubToUsd = 12.3f,
            usdToRub = 12.34f
        )
    }

    fun getDisplayCurrency(): String {
        return currencyPreferences.getString(DISPLAY_CURRENCY_KEY, RUB_RATE)!!
    }

    fun saveDisplayCurrency(currency: String) {
        currencyPreferences.edit {
            putString(DISPLAY_CURRENCY_KEY, currency)
        }
    }

}