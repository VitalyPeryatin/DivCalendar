package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.repositories.SettingsRepository

class SettingsInteractor {
    fun saveIsAccountTaxes(isAccountTaxes: Boolean) {
        SettingsRepository.saveIsAccountTaxes(isAccountTaxes)
    }

    fun isIncludeTaxes(): Boolean {
        return SettingsRepository.isIncludeTaxes()
    }

    fun sendFeedback(message: String) {
        SettingsRepository.sendFeedback(message)
    }
}