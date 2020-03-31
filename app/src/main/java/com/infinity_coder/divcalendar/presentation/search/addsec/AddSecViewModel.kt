package com.infinity_coder.divcalendar.presentation.search.addsec

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.data.db.model.SecPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel
import com.infinity_coder.divcalendar.presentation._common.LiveEvent

class AddSecViewModel : ViewModel() {

    private val _secPackage = MutableLiveData<SecPackageDbModel>()
    val secPackage: LiveData<SecPackageDbModel>
        get() = _secPackage

    val shakePriceEditText = LiveEvent<Void?>()
    val shakeCountEditText = LiveEvent<Void?>()

    private val stockTotalPriceLiveData = MutableLiveData<Float>(0f)
    private lateinit var stock: ShortStockNetworkModel

    private var price: Float = 0f
    private var count: Int = 0

    fun getTotalStockPriceLiveData(): LiveData<Float?> {
        return stockTotalPriceLiveData
    }

    fun setSecurityPrice(price: Float) {
        this.price = price
        stockTotalPriceLiveData.postValue(price * count)
    }

    fun setStockCount(count: Int) {
        this.count = count
        stockTotalPriceLiveData.postValue(price * count)
    }

    fun setStock(stock: ShortStockNetworkModel) {
        this.stock = stock
    }

    private fun getStockPackage(): SecPackageDbModel {
        return SecPackageDbModel(stock.secid, stock.name, count, price)
    }

    fun addSecPackage() {
        when {
            price <= 0 -> {
                shakePriceEditText.postValue(null)
            }
            count <= 0 -> {
                shakeCountEditText.postValue(null)
            }
            else -> {
                val stockPackage = getStockPackage()
                _secPackage.postValue(stockPackage)
            }
        }
    }
}