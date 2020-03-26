package com.infinity_coder.divcalendar.data.repositories

import androidx.lifecycle.LiveData
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.StockPackageDbModel
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel

object StockRepository {

    private val stockDao = DivCalendarDatabase.roomDatabase.stockDao
    private val moexApi = RetrofitService.moexApi

    suspend fun loadAllStocks(): List<ShortStockNetworkModel> {
        return moexApi.getStocks().stocks
    }

    fun loadAllStockPackages(): LiveData<List<StockPackageDbModel>> {
        return stockDao.getAllStockPackageLiveData()
    }

    suspend fun appendStockPackage(newStockPackage: StockPackageDbModel) {
        var stockPackage = stockDao.getStockPackage(newStockPackage.secid)
        if (stockPackage == null) {
            stockPackage = newStockPackage
        } else {
            stockPackage.count += newStockPackage.count
            stockPackage.totalPrice += newStockPackage.totalPrice
        }
        stockDao.addStockPackage(stockPackage)
    }
}