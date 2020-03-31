package com.infinity_coder.divcalendar.data.repositories

import androidx.lifecycle.LiveData
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.SecPackageDbModel
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel

object SecRepository {

    private val stockDao = DivCalendarDatabase.roomDatabase.stockDao
    private val moexApi = RetrofitService.moexApi

    suspend fun loadAllStocks(): List<ShortStockNetworkModel> {
        return moexApi.getStocks().stocks
    }

    fun loadAllStockPackages(): LiveData<List<SecPackageDbModel>> {
        return stockDao.getAllStockPackageLiveData()
    }

    suspend fun appendStockPackage(newStockPackage: SecPackageDbModel) {
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