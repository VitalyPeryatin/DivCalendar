package com.infinity_coder.divcalendar.presentation.portfolio.managesecurity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetworkModel
import com.infinity_coder.divcalendar.presentation._common.LiveEvent

class ChangeSecurityViewModel : ViewModel() {

    private val _changeSecurityPackage = MutableLiveData<SecurityPackageDbModel>()
    val changeSecurityPackage: LiveData<SecurityPackageDbModel>
        get() = _changeSecurityPackage

    val shakePriceEditText = LiveEvent<Void?>()
    val shakeCountEditText = LiveEvent<Void?>()

    private lateinit var security: SecurityNetworkModel

    private var cost: Float = 0f
    private var count: Int = 0

    fun setPackageCost(price: Float) {
        this.cost = price
    }

    fun setPackageCount(count: Int) {
        this.count = count
    }

    fun setSecurity(security: SecurityNetworkModel) {
        this.security = security
    }

    private fun getSecurityPackage(count: Int, price: Float): SecurityPackageDbModel {
        return SecurityPackageDbModel(security.ticker, security.name, count, price)
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
        val securityPackage = getSecurityPackage(0, 0f)
        _changeSecurityPackage.value = securityPackage
    }
}