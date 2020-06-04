package com.infinity_coder.divcalendar.presentation._common.delegate

import android.content.Context
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel.Companion.SECURITY_MARKET_FOREIGN
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel.Companion.SECURITY_MARKET_RUSSIAN

object SecurityMarketDelegate {

    private val securityMarkets = arrayOf(SECURITY_MARKET_RUSSIAN, SECURITY_MARKET_FOREIGN)

    fun getFirstMarket(): String {
        return securityMarkets.first()
    }

    fun getTitles(context: Context): List<String>{
        return securityMarkets.map { getTitle(context, it) }
    }

    private fun getTitle(context: Context, market: String): String {
        return when (market) {
            SECURITY_MARKET_RUSSIAN -> context.resources.getString(R.string.russian)
            SECURITY_MARKET_FOREIGN -> context.resources.getString(R.string.foreign)
            else -> throw IllegalStateException("No title for such market: $market")
        }
    }

    fun getMarketByTitle(context: Context, title: String): String {
        return when (title) {
            context.resources.getString(R.string.russian) -> SECURITY_MARKET_RUSSIAN
            context.resources.getString(R.string.foreign) -> SECURITY_MARKET_FOREIGN
            else -> throw IllegalStateException("No market for such title: $title")
        }
    }

    fun getMarketIndex(market: String): Int {
        return securityMarkets.indexOf(market)
    }
}