package com.infinity_coder.divcalendar.presentation.portfolio.dialogs.changesecurity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.domain.SecurityInteractor
import com.infinity_coder.divcalendar.presentation._common.LiveEvent
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ChangeSecurityViewModel : ViewModel() {

    val securityPackageChanged = LiveEvent<Void?>()
    val shakePriceEditText = LiveEvent<Void?>()
    val shakeCountEditText = LiveEvent<Void?>()

    private val securityInteractor = SecurityInteractor()

    private var cost: BigDecimal = BigDecimal.ZERO
    private var count: BigDecimal = BigDecimal.ZERO

    fun setPackageCost(price: BigDecimal) {
        this.cost = price
    }

    fun setPackageCount(count: BigDecimal) {
        this.count = count
    }

    fun changePackage(securityPackage: SecurityDbModel) = viewModelScope.launch {
        when {
            cost <= BigDecimal.ZERO -> {
                shakePriceEditText.value = null
            }
            count <= BigDecimal.ZERO -> {
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