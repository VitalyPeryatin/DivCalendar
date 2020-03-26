package com.infinity_coder.divcalendar.presentation.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.data.db.model.StockPackageDbModel
import com.infinity_coder.divcalendar.data.repositories.StockRepository

class PortfolioViewModel : ViewModel() {

    fun getStocksLiveData(): LiveData<List<StockPackageDbModel>> =
        StockRepository.loadAllStockPackages()

}