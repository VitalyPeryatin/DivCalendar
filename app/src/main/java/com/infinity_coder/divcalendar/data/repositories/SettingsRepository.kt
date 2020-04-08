package com.infinity_coder.divcalendar.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.infinity_coder.divcalendar.presentation.App

object SettingsRepository {

    private const val TAXES_PREFERENCES_NAME = "Settings"

    private const val ACCOUNT_TAXES_PREF_KEY = "is_account_taxes"

    private val taxesPreferences = App.instance.getSharedPreferences(TAXES_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun saveIsAccountTaxes(isAccountTaxes: Boolean) {
        taxesPreferences.edit {
            putBoolean(ACCOUNT_TAXES_PREF_KEY, isAccountTaxes)
        }
    }

    fun isAccountTaxes(): Boolean {
        return taxesPreferences.getBoolean(ACCOUNT_TAXES_PREF_KEY, false)
    }

    fun sendFeedback(message: String) {
    }
}