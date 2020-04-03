package com.infinity_coder.divcalendar.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.presentation._common.SecurityMarketDelegate

class SearchSecurityViewModel : ViewModel() {

    private val _marketLiveData = MutableLiveData(SecurityMarketDelegate.SECURITY_MARKET_RUSSIAN)
    val marketLiveData: LiveData<String>
        get() = _marketLiveData

    private val _queryLiveData = MutableLiveData<String>()
    val queryLiveData: LiveData<String>
        get() = _queryLiveData

    fun getCurrentMarketIndex(): Int {
        return SecurityMarketDelegate.getMarketIndex(_marketLiveData.value!!)
    }

    fun setMarket(market: String) {
        _marketLiveData.value = market
    }

    fun executeQuery(query: String) {
        _queryLiveData.value = query
    }
}