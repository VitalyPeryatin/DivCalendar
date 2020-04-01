package com.infinity_coder.divcalendar.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortSecurityNetworkModel
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import kotlinx.coroutines.launch

class SearchSecurityViewModel : ViewModel() {

    private val _filteredSecuritiesLiveData = MutableLiveData<List<ShortSecurityNetworkModel>>()
    val filteredSecuritiesLiveData: LiveData<List<ShortSecurityNetworkModel>>
        get() = _filteredSecuritiesLiveData

    private var allSecurities: List<ShortSecurityNetworkModel> = emptyList()

    private val portfolioInteractor = PortfolioInteractor()

    init {
        loadAllSecurities()
    }

    fun appendSecurityPackage(securityPackage: SecurityPackageDbModel) = viewModelScope.launch {
        portfolioInteractor.appendSecurityPackage(securityPackage)
    }

    fun requestSecuritiesByQuery(query: String) {
        if (query.isBlank()) {
            _filteredSecuritiesLiveData.postValue(emptyList())
            return
        }

        val filteredSecurities = allSecurities.filter {
            it.name.contains(query) || it.secid.contains(query)
        }.take(20)
        _filteredSecuritiesLiveData.postValue(filteredSecurities)
    }

    private fun loadAllSecurities() = viewModelScope.launch {
        val securities = portfolioInteractor.loadAllSecurities()
        allSecurities = securities
    }

}