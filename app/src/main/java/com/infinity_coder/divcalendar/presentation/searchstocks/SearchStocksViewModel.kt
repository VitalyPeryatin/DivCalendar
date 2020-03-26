package com.infinity_coder.divcalendar.presentation.searchstocks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.StockPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel
import com.infinity_coder.divcalendar.data.repositories.StockRepository
import kotlinx.coroutines.launch

class SearchStocksViewModel : ViewModel() {

    private val filteredStocksLiveData = MutableLiveData<List<ShortStockNetworkModel>>()

    private var allStocks: List<ShortStockNetworkModel> = emptyList()

    init {
        loadAllStock()
    }

    fun getFilteredStocksLiveData(): LiveData<List<ShortStockNetworkModel>> {
        return filteredStocksLiveData
    }

    fun appendStockPackage(stockPackage: StockPackageDbModel) = viewModelScope.launch {
        StockRepository.appendStockPackage(stockPackage)
    }

    fun requestStocksByQuery(query: String) {
        if (query.isBlank()) {
            filteredStocksLiveData.postValue(emptyList())
            return
        }

        val filteredStocks = allStocks.filter {
            it.name.contains(query) || it.secid.contains(query)
        }.take(20)
        filteredStocksLiveData.postValue(filteredStocks)
    }

    private fun loadAllStock() = viewModelScope.launch {
        val stocks = StockRepository.loadAllStocks()
        allStocks = stocks
    }

}