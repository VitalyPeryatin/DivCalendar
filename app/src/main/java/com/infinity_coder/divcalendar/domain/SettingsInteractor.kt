package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.repositories.SettingsRepository

class SettingsInteractor {
    fun saveIsAccountTaxes(isAccountTaxes: Boolean) {
        SettingsRepository.saveIsIncludeTaxes(isAccountTaxes)
    }

    fun isIncludeTaxes(): Boolean {
        return SettingsRepository.isIncludeTaxes()
    }

    fun isHideCopecks(): Boolean {
        return SettingsRepository.isHideCopecks()
    }

    fun saveIsHideCopecks(ishideCopecks: Boolean) {
        SettingsRepository.saveIsHideCopecks(ishideCopecks)
    }

    fun reportError(message: String) {
        SettingsRepository.reportError(message)
    }
}