package com.infinity_coder.divcalendar.presentation.search.addsecurity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetworkModel
import com.infinity_coder.divcalendar.presentation._common.LiveEvent

class AddSecurityViewModel : ViewModel() {

    private val _securityPackage = MutableLiveData<SecurityPackageDbModel>()
    val securityPackage: LiveData<SecurityPackageDbModel>
        get() = _securityPackage

    val shakePriceEditText = LiveEvent<Void?>()
    val shakeCountEditText = LiveEvent<Void?>()

    private val securityTotalPriceLiveData = MutableLiveData<Float>(0f)
    private lateinit var security: SecurityNetworkModel

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

    fun setSecurity(security: SecurityNetworkModel) {
        this.security = security
    }

    private fun getSecurityPackage() = SecurityPackageDbModel(
        secid = security.ticker,
        name = security.name,
        count = count,
        totalPrice = price * count
    )

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