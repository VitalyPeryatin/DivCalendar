package com.infinity_coder.divcalendar.presentation.portfolio.changepackage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortSecurityNetworkModel
import com.infinity_coder.divcalendar.presentation._common.LiveEvent

class ChangePackageViewModel : ViewModel() {

    private val _changeSecurityPackage = MutableLiveData<SecurityPackageDbModel>()
    val changeSecurityPackage: LiveData<SecurityPackageDbModel>
        get() = _changeSecurityPackage

    val shakePriceEditText = LiveEvent<Void?>()
    val shakeCountEditText = LiveEvent<Void?>()

    private lateinit var security: ShortSecurityNetworkModel

    private var cost: Float = 0f
    private var count: Int = 0

    fun setPackageCost(price: Float) {
        this.cost = price
    }

    fun setPackageCount(count: Int) {
        this.count = count
    }

    fun setSecurity(security: ShortSecurityNetworkModel) {
        this.security = security
    }

    private fun getSecurityPackage(count: Int, price: Float): SecurityPackageDbModel {
        return SecurityPackageDbModel(security.secid, security.name, count, price)
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
                _changeSecurityPackage.postValue(securityPackage)
            }
        }
    }

    fun removePackage() {
        val securityPackage = getSecurityPackage(0, 0f)
        _changeSecurityPackage.postValue(securityPackage)
    }
}