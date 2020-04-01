package com.infinity_coder.divcalendar.presentation._common

import android.content.Context
import com.infinity_coder.divcalendar.R

object SecurityTypeDelegate {

    const val SEC_TYP_STOCK = "stock"
    private const val SEC_TYP_BOND = "bond"

    @Suppress("DEPRECATION")
    fun getColor(context: Context, securityType: String): Int {

        val colorId = when(securityType) {
            SEC_TYP_STOCK -> R.color.typeRusStockColor
            SEC_TYP_BOND -> R.color.typeRusBondColor
            else -> R.color.typeRusStockColor
        }
        return context.getColorExt(colorId)
    }
}