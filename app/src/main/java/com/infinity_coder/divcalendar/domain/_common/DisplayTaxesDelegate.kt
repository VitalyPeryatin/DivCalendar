package com.infinity_coder.divcalendar.domain._common

import com.infinity_coder.divcalendar.domain.SettingsInteractor

object DisplayTaxesDelegate {

    private val settingInteractor = SettingsInteractor()

    fun displayTaxes(sourceText: String): String {
        var totalText = sourceText

        if (settingInteractor.isIncludeTaxes()) {
            totalText += " *"
        }
        return totalText
    }

}