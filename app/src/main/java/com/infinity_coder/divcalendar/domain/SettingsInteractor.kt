package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.repositories.SettingsRepository

class SettingsInteractor {
    fun saveIsAccountTaxes(isAccountTaxes: Boolean) {
        SettingsRepository.saveIsIncludeTaxes(isAccountTaxes)
    }

    fun isIncludeTaxes(): Boolean {
        return SettingsRepository.isIncludeTaxes()
    }

    fun saveIsHideCopecks(isHideCopecks: Boolean) {
        SettingsRepository.saveIsHideCopecks(isHideCopecks)
    }

    fun isHideCopecks(): Boolean {
        return SettingsRepository.isHideCopecks()
    }

    fun saveIsScrollingCalendarForCurrentMonth(isScrollingCalendarForCurrentMonth: Boolean){
        SettingsRepository.saveIsScrollingCalendarForCurrentMonth(isScrollingCalendarForCurrentMonth)
    }

    fun isScrollingCalendarForCurrentMonth(): Boolean{
        return SettingsRepository.isScrollingCalendarForCurrentMonth()
    }

    fun reportError(message: String) {
        SettingsRepository.reportError(message)
    }
}