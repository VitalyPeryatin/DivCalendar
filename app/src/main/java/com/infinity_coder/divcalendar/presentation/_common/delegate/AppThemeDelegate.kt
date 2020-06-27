package com.infinity_coder.divcalendar.presentation._common.delegate

import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.infinity_coder.divcalendar.data.repositories.SettingsRepository

object AppThemeDelegate {

    fun setAppTheme(activity: AppCompatActivity) {

        val nightModeFlags = activity.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)
        val typeTheme = SettingsRepository.getThemeType()

        if (nightModeFlags != typeTheme) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                activity.delegate.localNightMode = typeTheme
            } else {
                AppCompatDelegate.setDefaultNightMode(typeTheme)
            }
        }
    }
}