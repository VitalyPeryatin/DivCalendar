package com.infinity_coder.divcalendar.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetworkModel
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import com.infinity_coder.divcalendar.domain.SearchInteractor
import com.infinity_coder.divcalendar.presentation._common.SecurityTypeDelegate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchSecurityViewModel : ViewModel() {

    private val _searchedSecurities = MutableLiveData<List<SecurityNetworkModel>>()
    val searchedSecurities: LiveData<List<SecurityNetworkModel>>
        get() = _searchedSecurities

    private val _state = MutableLiveData<Int>(VIEW_STATE_SEARCH_SECURITY_START_SEARCH)
    val state: LiveData<Int>
        get() = _state

    private val _securityType = MutableLiveData<String>(SecurityTypeDelegate.SECURITY_TYPE_STOCK)
    val securityType: LiveData<String>
        get() = _securityType

    private val _market = MutableLiveData<String>(SECURITY_MARKET_RUSSIAN)
    val market: LiveData<String>
        get() = _market

    private val searchInteractor = SearchInteractor()
    private val portfolioInteractor = PortfolioInteractor()

    fun appendSecurityPackage(securityPackage: SecurityPackageDbModel) = viewModelScope.launch {
        portfolioInteractor.appendSecurityPackage(securityPackage)
    }

    fun search(query: String) = viewModelScope.launch {
        val type = securityType.value!!
        val market = market.value!!

        if (query.length >= MIN_QUERY_LENGTH) {
            try {
                searchInteractor.search(query, type, market)
                    .flowOn(Dispatchers.IO)
                    .onStart { _state.postValue(VIEW_STATE_SEARCH_SECURITY_LOADING) }
                    .onEach(this@SearchSecurityViewModel::collectSearchSecurities)
                    .launchIn(viewModelScope)
            } catch (e: Exception) {
                _state.postValue(VIEW_STATE_SEARCH_SECURITY_NO_NETWORK)
            }
        } else {
            _state.postValue(VIEW_STATE_SEARCH_SECURITY_START_SEARCH)
        }
    }

    private suspend fun collectSearchSecurities(searchSecurities: List<SecurityNetworkModel>) {
        this._searchedSecurities.postValue(searchSecurities)

        if (searchSecurities.isEmpty()) {
            _state.postValue(VIEW_STATE_SEARCH_SECURITY_EMPTY)
        } else {
            _state.postValue(VIEW_STATE_SEARCH_SECURITY_CONTENT)
        }
    }

    companion object {
        const val VIEW_STATE_SEARCH_SECURITY_LOADING = 1
        const val VIEW_STATE_SEARCH_SECURITY_CONTENT = 2
        const val VIEW_STATE_SEARCH_SECURITY_EMPTY = 3
        const val VIEW_STATE_SEARCH_SECURITY_NO_NETWORK = 4
        const val VIEW_STATE_SEARCH_SECURITY_START_SEARCH = 5

        private const val SECURITY_MARKET_RUSSIAN = "russian"
        private const val SECURITY_MARKET_FOREIGN = "foreign"

        private const val MIN_QUERY_LENGTH = 3
    }

}