package com.infinity_coder.divcalendar.presentation._common

import android.content.Context
import com.infinity_coder.divcalendar.R

object SecurityTypeDelegate {

    const val SECURITY_TYPE_STOCK = "stock"
    private const val SECURITY_TYPE_BOND = "bond"

    @Suppress("DEPRECATION")
    fun getColor(context: Context, securityType: String): Int {

        val colorId = when (securityType) {
            SECURITY_TYPE_STOCK -> R.color.typeRusStockColor
            SECURITY_TYPE_BOND -> R.color.typeRusBondColor
            else -> R.color.typeRusStockColor
        }
        return context.getColorExt(colorId)
    }
}