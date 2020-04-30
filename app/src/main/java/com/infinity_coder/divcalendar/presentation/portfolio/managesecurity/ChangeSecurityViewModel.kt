package com.infinity_coder.divcalendar.presentation.portfolio.managesecurity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.domain.SecurityInteractor
import com.infinity_coder.divcalendar.presentation._common.LiveEvent
import kotlinx.coroutines.launch

class ChangeSecurityViewModel : ViewModel() {

    private val _changeSecurity = MutableLiveData<SecurityDbModel>()
    val changeSecurity: LiveData<SecurityDbModel>
        get() = _changeSecurity

    val shakePriceEditText = LiveEvent<Void?>()
    val shakeCountEditText = LiveEvent<Void?>()

    private val securityInteractor = SecurityInteractor()

    private lateinit var securityIsin: String

    private var cost: Double = 0.0
    private var count: Int = 0

    fun setPackageCost(price: Double) {
        this.cost = price
    }

    fun setPackageCount(count: Int) {
        this.count = count
    }

    fun setSecurityIsin(isin: String) {
        this.securityIsin = isin
    }

    private suspend fun getSecurity(count: Int, price: Double): SecurityDbModel? {
        return securityInteractor.getSecurityByIsin(securityIsin)?.apply {
            this.count = count
            this.totalPrice = price
        }
    }

    fun changePackage() = viewModelScope.launch {
        when {
            cost <= 0 -> {
                shakePriceEditText.postValue(null)
            }
            count <= 0 -> {
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
        val security = getSecurity(0, 0.0)
        if (security != null) {
            _changeSecurity.value = security
        }
    }
}