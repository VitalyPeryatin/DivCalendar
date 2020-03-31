package com.infinity_coder.divcalendar.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortSecNetworkModel
import com.infinity_coder.divcalendar.data.repositories.SecurityRepository
import kotlinx.coroutines.launch

class SearchSecViewModel : ViewModel() {

    private val _filteredSecuritiesLiveData = MutableLiveData<List<ShortSecNetworkModel>>()
    val filteredSecuritiesLiveData: LiveData<List<ShortSecNetworkModel>>
        get() = _filteredSecuritiesLiveData

    private var allSecurities: List<ShortSecNetworkModel> = emptyList()

    init {
        loadAllSecurities()
    }

    fun appendSecurityPackage(securityPackage: SecurityPackageDbModel) = viewModelScope.launch {
        SecurityRepository.appendSecurityPackage(securityPackage)
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
        val securities = SecurityRepository.loadAllSecurities()
        allSecurities = securities
    }

}