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

    private val _hideCopecks = MutableLiveData(settingsInteractor.isHideCopecks())
    val hideCopecks: LiveData<Boolean>
        get() = _hideCopecks

    private val _isScrollingCalendarForCurrentMonth = MutableLiveData(settingsInteractor.isScrollingCalendarForCurrentMonth())
    val isScrollingCalendarForCurrentMonth: LiveData<Boolean>
        get() = _isScrollingCalendarForCurrentMonth

    fun saveIsAccountTaxes(isAccountTaxes: Boolean) {
        _isAccountTaxes.value = isAccountTaxes
        settingsInteractor.saveIsAccountTaxes(isAccountTaxes)
    }

    fun saveIsHideCopecks(isHideCopecks: Boolean) {
        _hideCopecks.value = isHideCopecks
        settingsInteractor.saveIsHideCopecks(isHideCopecks)
    }

    fun saveIsScrollingCalendarForCurrentMonth(isScrollingCalendarForCurrentMonth: Boolean){
        _isScrollingCalendarForCurrentMonth.value = isScrollingCalendarForCurrentMonth
        settingsInteractor.saveIsScrollingCalendarForCurrentMonth(isScrollingCalendarForCurrentMonth)
    }

    fun reportError(message: String) {
        settingsInteractor.reportError(message)
    }
}