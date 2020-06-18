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
import java.math.BigDecimal

class AddSecurityViewModel : ViewModel() {

    private val _securityTotalPriceMutableLiveData = MutableLiveData(BigDecimal.ZERO)
    val securityTotalPriceLiveDate: LiveData<BigDecimal>
        get() = _securityTotalPriceMutableLiveData

    val addSecurityIfHasSubscription = LiveEvent<SecurityDbModel>()
    val success = LiveEvent<Void?>()
    val shakePriceEditText = LiveEvent<Void?>()
    val shakeCountEditText = LiveEvent<Void?>()

    private val securityInteractor = SecurityInteractor()
    private val subscriptionInteractor = SubscriptionInteractor()

    private var price = BigDecimal.ZERO
    private var count: BigDecimal = BigDecimal.ZERO

    fun setSecurityPrice(price: BigDecimal) {
        this.price = price
        _securityTotalPriceMutableLiveData.value = price.multiply(count)
    }

    fun setSecurityCount(count: BigDecimal) {
        this.count = count
        _securityTotalPriceMutableLiveData.value = price.multiply(count)
    }

    fun appendSecurityPackage(securityPackage: SecurityDbModel) = viewModelScope.launch {
        securityInteractor.appendSecurityPackage(securityPackage)
        success.value = null
    }

    fun addSecurityPackage(securityNetModel: SecurityNetModel) = viewModelScope.launch {
        when {
            price <= BigDecimal.ZERO -> shakePriceEditText.value = null

            count <= BigDecimal.ZERO -> shakeCountEditText.value = null

            else -> {
                val security = buildSecurity(securityNetModel)
                withContext(Dispatchers.IO) {
                    security.color = securityInteractor.getColorForSecurityLogo(security.logo)
                }
                requestOnAppendSecurityPackage(security)
            }
        }
    }

    private fun buildSecurity(securityNetModel: SecurityNetModel): SecurityDbModel {
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