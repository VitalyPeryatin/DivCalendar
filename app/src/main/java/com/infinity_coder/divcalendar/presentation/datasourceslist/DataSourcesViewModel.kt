package com.infinity_coder.divcalendar.presentation.datasourceslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.repositories.datasources.models.DataSourceModel
import com.infinity_coder.divcalendar.domain.DataSourcesInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DataSourcesViewModel : ViewModel() {

    private val _dataSourceList = MutableLiveData<List<DataSourceModel>>()
    val dataSourceList: LiveData<List<DataSourceModel>>
        get() = _dataSourceList

    private val dataSourcesInteractor = DataSourcesInteractor()

    init {
        loadDataSources()
    }

    private fun loadDataSources() = viewModelScope.launch {
        dataSourcesInteractor.getDataSources()
            .onEach { _dataSourceList.value = it }
            .launchIn(viewModelScope)
    }

}