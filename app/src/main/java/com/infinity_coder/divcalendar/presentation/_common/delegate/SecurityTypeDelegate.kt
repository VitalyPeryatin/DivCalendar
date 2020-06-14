package com.infinity_coder.divcalendar.presentation._common.delegate

import android.content.Context
import androidx.core.content.ContextCompat
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel.Companion.SECURITY_TYPES
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel.Companion.SECURITY_TYPE_BOND
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel.Companion.SECURITY_TYPE_STOCK

object SecurityTypeDelegate {

    fun getColor(context: Context, type: String): Int {
        val colorId = when (type) {
            SECURITY_TYPE_STOCK -> R.color.typeStockColor
            SECURITY_TYPE_BOND -> R.color.typeBondColor
            else -> throw IllegalStateException("No color for such security type: $type")
        }
        return ContextCompat.getColor(context, colorId)
    }

    fun getTitle(context: Context, position: Int): String {
        return getTitle(context, SECURITY_TYPES[position])
    }

    fun getTitle(context: Context, type: String): String {
        return when (type) {
            SECURITY_TYPE_STOCK -> context.resources.getString(R.string.stocks)
            SECURITY_TYPE_BOND -> context.resources.getString(R.string.bonds)
            else -> throw IllegalStateException("No title for such security type: $type")
        }
    }

    fun getCellValueForExcel(context: Context, type: String): String {
        return when (type) {
            SECURITY_TYPE_STOCK -> context.resources.getString(R.string.cell_value_stock)
            SECURITY_TYPE_BOND -> context.resources.getString(R.string.cell_value_bond)
            else -> "-"
        }
    }
}