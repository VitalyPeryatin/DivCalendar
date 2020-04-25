package com.infinity_coder.divcalendar

import android.content.Context
import androidx.core.content.edit
import com.infinity_coder.divcalendar.presentation.App
import com.infinity_coder.divcalendar.presentation._common.extensions.getNotNullString

object AppConfig {

    private const val APP_CONFIG_PREF_NAME = "AppConfig"
    private val appConfigPreferences = App.instance.getSharedPreferences(APP_CONFIG_PREF_NAME, Context.MODE_PRIVATE)

    private const val PREF_KEY_APP_CONFIG = "appConfig"

    const val DEV = "DEV"
    const val PROD = "PROD"

    var serverConfig: String
        set(value) {
            appConfigPreferences.edit {
                if (value == DEV || value == PROD) {
                    putString(PREF_KEY_APP_CONFIG, value)
                }
            }
        }
        get() = appConfigPreferences.getNotNullString(PREF_KEY_APP_CONFIG, PROD)
}