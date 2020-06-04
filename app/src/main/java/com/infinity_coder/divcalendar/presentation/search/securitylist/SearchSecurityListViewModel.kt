package com.infinity_coder.divcalendar.presentation.search.securitylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import com.infinity_coder.divcalendar.domain.SearchInteractor
import com.infinity_coder.divcalendar.domain.models.QueryGroup
import com.infinity_coder.divcalendar.presentation._common.logException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchSecurityListViewModel : ViewModel() {

    private val _state = MutableLiveData(VIEW_STATE_SEARCH_SECURITY_CONTENT)
    val state: LiveData<Int>
        get() = _state

    private val _searchedSecurities = MutableLiveData<List<SecurityNetModel>>()
    val searchedSecurities: LiveData<List<SecurityNetModel>>
        get() = _searchedSecurities

    lateinit var securityType: String

    private val searchInteractor = SearchInteractor()

    private var oldQueryGroup: QueryGroup? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun search(query: String, market: String) = viewModelScope.launch {
        val queryGroup = QueryGroup(query, market, securityType)

        if (oldQueryGroup == queryGroup)
            return@launch

        oldQueryGroup = queryGroup

        searchInteractor.search(queryGroup)
            .flowOn(Dispatchers.IO)
            .onStart { _state.value = VIEW_STATE_SEARCH_SECURITY_LOADING }
            .onEach { _searchedSecurities.value = it }
            .catch {
                logException(this@SearchSecurityListViewModel, it)
                _state.value = VIEW_STATE_SEARCH_SECURITY_NO_NETWORK
            }
            .onCompletion { _state.value = VIEW_STATE_SEARCH_SECURITY_CONTENT }
            .launchIn(viewModelScope)

    }

    companion object {
        const val VIEW_STATE_SEARCH_SECURITY_LOADING = 1
        const val VIEW_STATE_SEARCH_SECURITY_CONTENT = 2
        const val VIEW_STATE_SEARCH_SECURITY_NO_NETWORK = 3
    }
}