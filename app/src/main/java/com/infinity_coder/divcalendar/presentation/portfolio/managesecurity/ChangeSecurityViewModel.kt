package com.infinity_coder.divcalendar.presentation.portfolio.managesecurity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.domain.SecurityInteractor
import com.infinity_coder.divcalendar.presentation._common.LiveEvent
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ChangeSecurityViewModel : ViewModel() {

    private val _changeSecurity = MutableLiveData<SecurityDbModel>()
    val changeSecurity: LiveData<SecurityDbModel>
        get() = _changeSecurity

    val shakePriceEditText = LiveEvent<Void?>()
    val shakeCountEditText = LiveEvent<Void?>()

    private val securityInteractor = SecurityInteractor()

    private lateinit var securityIsin: String

    private var cost: BigDecimal = BigDecimal(0.0)
    private var count: BigDecimal = BigDecimal.ZERO

    fun setPackageCost(price: BigDecimal) {
        this.cost = price
    }

    fun setPackageCount(count: BigDecimal) {
        this.count = count
    }

    fun setSecurityIsin(isin: String) {
        this.securityIsin = isin
    }

    private suspend fun getSecurity(count: BigDecimal, price: BigDecimal): SecurityDbModel? {
        return securityInteractor.getSecurityByIsin(securityIsin)?.apply {
            this.count = count
            this.totalPrice = price
        }
    }

    fun changePackage() = viewModelScope.launch {
            when {
            cost <= BigDecimal(0) -> {
                shakePriceEditText.postValue(null)
            }
            count <= BigDecimal.ZERO -> {
                shakeCountEditText.postValue(null)
            }
            else -> {
                val security = getSecurity(count, cost)
                if (security != null) {
                    _changeSecurity.value = security
                }
            }
        }
    }

    fun removePackage() = viewModelScope.launch {
        val security = getSecurity(BigDecimal.ZERO, BigDecimal(0.0))
        if (security != null) {
            _changeSecurity.value = security
        }
    }
}