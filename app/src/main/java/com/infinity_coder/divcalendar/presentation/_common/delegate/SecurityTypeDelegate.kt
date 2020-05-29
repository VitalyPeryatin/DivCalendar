package com.infinity_coder.divcalendar.presentation._common.delegate

import android.content.Context
import androidx.core.content.ContextCompat
import com.infinity_coder.divcalendar.R

object SecurityTypeDelegate {

    const val SECURITY_TYPE_STOCK = "stock"
    const val SECURITY_TYPE_BOND = "bond"

    val securityTypes = arrayOf(
        SECURITY_TYPE_STOCK,
        SECURITY_TYPE_BOND
    )

    @Suppress("DEPRECATION")
    fun getColor(context: Context, securityType: String): Int {
        val colorId = when (securityType) {
            SECURITY_TYPE_STOCK -> R.color.typeRusStockColor
            SECURITY_TYPE_BOND -> R.color.typeRusBondColor
            else -> R.color.typeRusStockColor
        }
        return ContextCompat.getColor(context, colorId)
    }

    fun getTitle(context: Context, type: String): String {
        return when (type) {
            SECURITY_TYPE_STOCK -> context.resources.getString(R.string.stocks)
            SECURITY_TYPE_BOND -> context.resources.getString(R.string.bonds)
            else -> ""
        }
    }

    fun getTitles(context: Context): List<String> =
        securityTypes.map {
            getTitle(
                context,
                it
            )
        }
}