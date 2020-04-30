package com.infinity_coder.divcalendar.presentation.search.addsecurity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import com.infinity_coder.divcalendar.domain.SecurityInteractor
import com.infinity_coder.divcalendar.presentation._common.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddSecurityViewModel : ViewModel() {

    private val _securityMutableLiveData = MutableLiveData<SecurityDbModel>()
    val securityLiveData: LiveData<SecurityDbModel>
        get() = _securityMutableLiveData

    private val securityInteractor = SecurityInteractor()

    val shakePriceEditText = LiveEvent<Void?>()
    val shakeCountEditText = LiveEvent<Void?>()

    private val securityTotalPriceLiveData = MutableLiveData(0.0)

    private var price: Double = 0.0
    private var count: Int = 0

    private lateinit var security: SecurityNetModel

    fun getTotalSecurityPriceLiveData(): LiveData<Double?> {
        return securityTotalPriceLiveData
    }

    fun setSecurityPrice(price: Double) {
        this.price = price
        securityTotalPriceLiveData.postValue(price * count)
    }

    fun setSecurityCount(count: Int) {
        this.count = count
        securityTotalPriceLiveData.postValue(price * count)
    }

    fun setSecurity(security: SecurityNetModel) {
        this.security = security
    }

    private fun buildSecurity() = SecurityDbModel(
        isin = security.isin,
        ticker = security.ticker,
        name = security.name,
        logo = security.logo,
        count = count,
        totalPrice = price * count,
        yearYield = security.yearYield,
        currency = security.currency,
        type = security.type
    )

    fun addSecurityPackage() = viewModelScope.launch {
        when {
            price <= 0 -> {
                shakePriceEditText.postValue(null)
            }
            count <= 0 -> {
                shakeCountEditText.postValue(null)
            }
            else -> {
                val security = buildSecurity()
                withContext(Dispatchers.IO) {
                    security.color = securityInteractor.getColorForSecurityLogo(security.logo)
                }
                _securityMutableLiveData.value = security
            }
        }
    }
}