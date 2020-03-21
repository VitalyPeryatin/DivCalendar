package com.infinity_coder.divcalendar.presentation.searchstocks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel

class SearchStocksViewModel : ViewModel() {

    private val stocksLiveData = MutableLiveData<List<ShortStockNetworkModel>>()

    private var allStocks: List<ShortStockNetworkModel> = emptyList()

    init {
        allStocks = listOf(
            ShortStockNetworkModel("AAPL", "Apple Inc."),
            ShortStockNetworkModel("ABRD", "Абрау-Дюрсо ПАО ао"),
            ShortStockNetworkModel("ACKO", "АСКО-СТРАХОВАНИЕ ПАО ао"),
            ShortStockNetworkModel("AFKS", "АФК \"Система\" ПАО ао"),
            ShortStockNetworkModel("AFLT", "Аэрофлот-росс.авиалин(ПАО)ао"),
            ShortStockNetworkModel("AKRN", "Акрон ПАО ао"),
            ShortStockNetworkModel("ALBK", "Бест Эффортс Банк ПАО ао"),
            ShortStockNetworkModel("ALNU", "АЛРОСА-Нюрба ПАО ао")
        )
    }

    fun getStocksLiveData(): LiveData<List<ShortStockNetworkModel>> {
        return stocksLiveData
    }

    fun requestStocksByQuery(query: String) {
        val filteredStocks = allStocks.filter {
            it.name.contains(query) || it.secid.contains(query)
        }
        stocksLiveData.postValue(filteredStocks)
    }

}