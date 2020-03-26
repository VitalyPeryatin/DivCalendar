package com.infinity_coder.divcalendar.presentation.searchstocks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.data.db.model.StockPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel

class AddStockViewModel : ViewModel() {

    private val stockTotalPriceLiveData = MutableLiveData<Float>(0f)
    private lateinit var stock: ShortStockNetworkModel

    private var count: Int = 0
    private var price: Float = 0f

    fun getTotalStockPriceLiveData(): LiveData<Float?> {
        return stockTotalPriceLiveData
    }

    fun setStockPrice(price: Float) {
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

    fun getStockPackage(): StockPackageDbModel {
        return StockPackageDbModel(stock.secid, stock.name, count, price)
    }
}