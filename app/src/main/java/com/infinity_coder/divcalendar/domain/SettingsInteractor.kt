package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.repositories.SettingsRepository

class SettingsInteractor {
    fun saveIsAccountTaxes(isAccountTaxes: Boolean) {
        SettingsRepository.saveIsIncludeTaxes(isAccountTaxes)
    }

    fun isIncludeTaxes(): Boolean {
        return SettingsRepository.isIncludeTaxes()
    }

    fun hideCopecks(): Boolean {
        return SettingsRepository.hideCopecks()
    }

    fun saveHideCopecks(hideCopecks: Boolean) {
        SettingsRepository.saveHideCopecks(hideCopecks)
    }

    fun reportError(message: String) {
        SettingsRepository.reportError(message)
    }
}