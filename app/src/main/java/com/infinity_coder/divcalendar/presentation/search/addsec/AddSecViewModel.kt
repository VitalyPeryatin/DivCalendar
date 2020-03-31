package com.infinity_coder.divcalendar.presentation.search.addsec

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortSecNetworkModel
import com.infinity_coder.divcalendar.presentation._common.LiveEvent

class AddSecViewModel : ViewModel() {

    private val _securityPackage = MutableLiveData<SecurityPackageDbModel>()
    val secPackage: LiveData<SecurityPackageDbModel>
        get() = _securityPackage

    val shakePriceEditText = LiveEvent<Void?>()
    val shakeCountEditText = LiveEvent<Void?>()

    private val securityTotalPriceLiveData = MutableLiveData<Float>(0f)
    private lateinit var security: ShortSecNetworkModel

    private var price: Float = 0f
    private var count: Int = 0

    fun getTotalSecurityPriceLiveData(): LiveData<Float?> {
        return securityTotalPriceLiveData
    }

    fun setSecurityPrice(price: Float) {
        this.price = price
        securityTotalPriceLiveData.postValue(price * count)
    }

    fun setSecurityCount(count: Int) {
        this.count = count
        securityTotalPriceLiveData.postValue(price * count)
    }

    fun setSecurity(security: ShortSecNetworkModel) {
        this.security = security
    }

    private fun getSecurityPackage(): SecurityPackageDbModel {
        return SecurityPackageDbModel(security.secid, security.name, count, price)
    }

    fun addSecurityPackage() {
        when {
            price <= 0 -> {
                shakePriceEditText.postValue(null)
            }
            count <= 0 -> {
                shakeCountEditText.postValue(null)
            }
            else -> {
                val securityPackage = getSecurityPackage()
                _securityPackage.postValue(securityPackage)
            }
        }
    }
}