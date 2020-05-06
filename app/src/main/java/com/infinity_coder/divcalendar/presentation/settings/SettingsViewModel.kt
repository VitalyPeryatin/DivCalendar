package com.infinity_coder.divcalendar.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.domain.SettingsInteractor

class SettingsViewModel : ViewModel() {

    val taxesValue = "13%"

    private val settingsInteractor = SettingsInteractor()

    private val _isAccountTaxes = MutableLiveData(settingsInteractor.isIncludeTaxes())
    val isAccountTaxes: LiveData<Boolean>
        get() = _isAccountTaxes

    fun saveIsAccountTaxes(isAccountTaxes: Boolean) {
        _isAccountTaxes.value = isAccountTaxes
        settingsInteractor.saveIsAccountTaxes(isAccountTaxes)
    }

    fun reportError(message: String) {
        settingsInteractor.reportError(message)
    }

    fun sendDataCast() {
        settingsInteractor.sendDataCast()
    }
}