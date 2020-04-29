package com.infinity_coder.divcalendar.presentation.portfolio.managesecurity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import com.infinity_coder.divcalendar.presentation._common.LiveEvent

class ChangeSecurityViewModel : ViewModel() {

    private val _changeSecurityPackage = MutableLiveData<SecurityDbModel>()
    val changeSecurityPackage: LiveData<SecurityDbModel>
        get() = _changeSecurityPackage

    val shakePriceEditText = LiveEvent<Void?>()
    val shakeCountEditText = LiveEvent<Void?>()

    private lateinit var security: SecurityNetModel

    private var cost: Double = 0.0
    private var count: Int = 0

    fun setPackageCost(price: Double) {
        this.cost = price
    }

    fun setPackageCount(count: Int) {
        this.count = count
    }

    fun setSecurity(security: SecurityNetModel) {
        this.security = security
    }

    private fun getSecurityPackage(count: Int, price: Double): SecurityDbModel {
        return SecurityDbModel(
            isin = security.isin,
            ticker = security.ticker,
            name = security.name,
            logo = security.logo,
            count = count,
            totalPrice = price,
            yearYield = security.yearYield,
            currency = security.currency,
            type = security.type
        )
    }

    fun changePackage() {
        when {
            cost <= 0 -> {
                shakePriceEditText.postValue(null)
            }
            count <= 0 -> {
                shakeCountEditText.postValue(null)
            }
            else -> {
                val securityPackage = getSecurityPackage(count, cost)
                _changeSecurityPackage.value = securityPackage
            }
        }
    }

    fun removePackage() {
        val securityPackage = getSecurityPackage(0, 0.0)
        _changeSecurityPackage.value = securityPackage
    }
}