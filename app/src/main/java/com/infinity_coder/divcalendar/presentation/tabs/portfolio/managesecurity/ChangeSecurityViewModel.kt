package com.infinity_coder.divcalendar.presentation.tabs.portfolio.managesecurity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.domain.SecurityInteractor
import com.infinity_coder.divcalendar.presentation._common.LiveEvent
import kotlinx.coroutines.launch

class ChangeSecurityViewModel : ViewModel() {

    val securityPackageChanged = LiveEvent<Void?>()
    val shakePriceEditText = LiveEvent<Void?>()
    val shakeCountEditText = LiveEvent<Void?>()

    private val securityInteractor = SecurityInteractor()

    private var cost: Double = 0.0
    private var count: Int = 0

    fun setPackageCost(price: Double) {
        this.cost = price
    }

    fun setPackageCount(count: Int) {
        this.count = count
    }

    fun changePackage(securityPackage: SecurityDbModel) = viewModelScope.launch {
        when {
            cost <= 0 -> {
                shakePriceEditText.value = null
            }
            count <= 0 -> {
                shakeCountEditText.value = null
            }
            else -> {
                securityPackage.count = count
                securityPackage.totalPrice = cost
                securityInteractor.changeSecurityPackage(securityPackage)
                securityPackageChanged.value = null
            }
        }
    }

    fun removePackage(securityPackage: SecurityDbModel) = viewModelScope.launch {
        securityInteractor.deleteSecurityPackage(securityPackage)
        securityPackageChanged.value = null
    }
}