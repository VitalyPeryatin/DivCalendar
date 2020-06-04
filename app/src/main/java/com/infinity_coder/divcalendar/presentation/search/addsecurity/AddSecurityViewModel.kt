package com.infinity_coder.divcalendar.presentation.search.addsecurity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import com.infinity_coder.divcalendar.domain.SecurityInteractor
import com.infinity_coder.divcalendar.domain.SubscriptionInteractor
import com.infinity_coder.divcalendar.presentation._common.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddSecurityViewModel : ViewModel() {

    private val _securityTotalPriceMutableLiveData = MutableLiveData(0.0)
    val securityTotalPriceLiveDate: LiveData<Double>
        get() = _securityTotalPriceMutableLiveData

    val addSecurityIfHasSubscription = LiveEvent<SecurityDbModel>()
    val success = LiveEvent<Void?>()
    val shakePriceEditText = LiveEvent<Void?>()
    val shakeCountEditText = LiveEvent<Void?>()

    private val securityInteractor = SecurityInteractor()
    private val subscriptionInteractor = SubscriptionInteractor()

    private var price: Double = 0.0
    private var count: Int = 0

    fun setSecurityPrice(price: Double) {
        this.price = price
        _securityTotalPriceMutableLiveData.value = price * count
    }

    fun setSecurityCount(count: Int) {
        this.count = count
        _securityTotalPriceMutableLiveData.value = price * count
    }

    fun appendSecurityPackage(securityPackage: SecurityDbModel) = viewModelScope.launch {
        securityInteractor.appendSecurityPackage(securityPackage)
        success.value = null
    }

    fun addSecurityPackage(securityNetModel: SecurityNetModel) = viewModelScope.launch {
        when {
            price <= 0 -> shakePriceEditText.value = null

            count <= 0 -> shakeCountEditText.value = null

            else -> {
                val security = buildSecurity(securityNetModel)
                withContext(Dispatchers.IO) {
                    security.color = securityInteractor.getColorForSecurityLogo(security.logo)
                }
                requestOnAppendSecurityPackage(security)
            }
        }
    }

    private fun buildSecurity(securityNetModel: SecurityNetModel):SecurityDbModel {
        return SecurityDbModel.from(securityNetModel).let {
            it.count = count
            it.totalPrice = price * count
            it
        }
    }

    private fun requestOnAppendSecurityPackage(securityPackage: SecurityDbModel) = viewModelScope.launch {
        if (subscriptionInteractor.isSecurityCountLeastThanSubscriptionGrant()) {
            appendSecurityPackage(securityPackage)
        } else {
            addSecurityIfHasSubscription.value = securityPackage
        }
    }
}