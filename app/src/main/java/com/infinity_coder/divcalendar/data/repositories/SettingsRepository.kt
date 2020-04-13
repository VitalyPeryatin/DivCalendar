package com.infinity_coder.divcalendar.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.infinity_coder.divcalendar.presentation.App

object SettingsRepository {

    private const val TAXES_PREFERENCES_NAME = "Settings"

    private const val INCLUDE_TAXES = "is_include_taxes"

    private val taxesPreferences = App.instance.getSharedPreferences(TAXES_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun saveIsAccountTaxes(isAccountTaxes: Boolean) {
        taxesPreferences.edit {
            putBoolean(INCLUDE_TAXES, isAccountTaxes)
        }
    }

    fun isIncludeTaxes(): Boolean {
        return taxesPreferences.getBoolean(INCLUDE_TAXES, false)
    }

    fun sendFeedback(message: String) {
    }
}