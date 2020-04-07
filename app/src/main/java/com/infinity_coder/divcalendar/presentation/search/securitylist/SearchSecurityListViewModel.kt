package com.infinity_coder.divcalendar.presentation.search.securitylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetworkModel
import com.infinity_coder.divcalendar.domain.SearchInteractor
import com.infinity_coder.divcalendar.domain.SecurityInteractor
import com.infinity_coder.divcalendar.presentation.search.model.QueryGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchSecurityListViewModel : ViewModel() {

    private val searchInteractor = SearchInteractor()
    private val portfolioInteractor = SecurityInteractor()

    private var loadedMarket: String? = null
    private var loadedQuery: String? = null

    lateinit var securityType: String

    private val _state = MutableLiveData(VIEW_STATE_SEARCH_SECURITY_CONTENT)
    val state: LiveData<Int>
        get() = _state

    private val _searchedSecurities = MutableLiveData<List<SecurityNetworkModel>>()
    val searchedSecurities: LiveData<List<SecurityNetworkModel>>
        get() = _searchedSecurities

    private var oldQueryGroup: QueryGroup? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun search(query: String = loadedQuery!!, market: String) = viewModelScope.launch {
        val queryGroup = QueryGroup(query, market, securityType)
        if (oldQueryGroup == queryGroup) return@launch

        searchInteractor.search(queryGroup)
            .flowOn(Dispatchers.IO)
            .onStart { _state.value = VIEW_STATE_SEARCH_SECURITY_LOADING }
            .onEach { _searchedSecurities.value = it }
            .catch { _state.value = VIEW_STATE_SEARCH_SECURITY_NO_NETWORK }
            .onCompletion { _state.value = VIEW_STATE_SEARCH_SECURITY_CONTENT }
            .launchIn(viewModelScope)

        saveQueryGroup(query, market)
    }

    private fun saveQueryGroup(query: String, market: String) {
        loadedQuery = query
        loadedMarket = market
    }

    fun appendSecurityPackage(securityPackage: SecurityPackageDbModel) = viewModelScope.launch {
        portfolioInteractor.appendSecurityPackage(securityPackage)
    }

    companion object {
        const val VIEW_STATE_SEARCH_SECURITY_LOADING = 1
        const val VIEW_STATE_SEARCH_SECURITY_CONTENT = 2
        const val VIEW_STATE_SEARCH_SECURITY_NO_NETWORK = 3
    }
}