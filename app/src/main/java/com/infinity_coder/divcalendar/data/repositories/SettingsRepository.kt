package com.infinity_coder.divcalendar.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.infinity_coder.divcalendar.presentation.App

object SettingsRepository {

    private const val TAXES_PREFERENCES_NAME = "Settings"

    private const val PREF_INCLUDE_TAXES = "is_include_taxes"

    const val TELEGRAM_GROUP_LINK = "https://t.me/joinchat/H2bVmxsrivVBQ0rqv0AWIg"

    private val taxesPreferences = App.instance.getSharedPreferences(TAXES_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun saveIsIncludeTaxes(isAccountTaxes: Boolean) {
        taxesPreferences.edit {
            putBoolean(PREF_INCLUDE_TAXES, isAccountTaxes)
        }
    }

    fun isIncludeTaxes(): Boolean {
        return taxesPreferences.getBoolean(PREF_INCLUDE_TAXES, false)
    }

    fun sendFeedback(message: String) {
    }
}