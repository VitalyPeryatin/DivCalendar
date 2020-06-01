package com.infinity_coder.divcalendar.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityMarketDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SearchSecurityViewModel : ViewModel() {

    private val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    @OptIn(FlowPreview::class)
    private val queryFlow = queryChannel.asFlow().debounce(QUERY_DELAY)

    private val _marketLiveData = MutableLiveData(SecurityMarketDelegate.SECURITY_MARKET_RUSSIAN)
    val marketLiveData: LiveData<String>
        get() = _marketLiveData

    private val _queryLiveData = MutableLiveData<String>()
    val queryLiveData: LiveData<String>
        get() = _queryLiveData

    init {
        viewModelScope.launch {
            queryFlow.collect { _queryLiveData.value = it }
        }
    }

    fun getCurrentMarketIndex(): Int {
        return SecurityMarketDelegate.getMarketIndex(_marketLiveData.value!!)
    }

    fun setMarket(market: String) {
        _marketLiveData.value = market
    }

    fun executeQuery(query: String) = viewModelScope.launch {
        queryChannel.send(query)
    }

    companion object {
        private const val QUERY_DELAY = 500L
    }
}