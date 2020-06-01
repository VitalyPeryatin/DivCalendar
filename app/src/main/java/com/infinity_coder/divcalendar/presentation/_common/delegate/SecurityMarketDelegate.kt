package com.infinity_coder.divcalendar.presentation._common.delegate

import android.content.Context
import com.infinity_coder.divcalendar.R

object SecurityMarketDelegate {

    const val SECURITY_MARKET_RUSSIAN = "russian"
    const val SECURITY_MARKET_FOREIGN = "foreign"

    private val securityMarkets = arrayOf(
        SECURITY_MARKET_RUSSIAN,
        SECURITY_MARKET_FOREIGN
    )

    private fun getTitle(context: Context, market: String): String {
        return when (market) {
            SECURITY_MARKET_RUSSIAN -> context.resources.getString(R.string.russian)
            SECURITY_MARKET_FOREIGN -> context.resources.getString(R.string.foreign)
            else -> throw IllegalStateException("No title for such market: $market")
        }
    }

    fun getTitles(context: Context): List<String> =
        securityMarkets.map {
            getTitle(
                context,
                it
            )
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