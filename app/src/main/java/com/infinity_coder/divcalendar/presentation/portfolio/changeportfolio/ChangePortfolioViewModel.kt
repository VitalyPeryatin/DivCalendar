package com.infinity_coder.divcalendar.presentation.portfolio.changeportfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangePortfolioViewModel : ViewModel() {

    private val _portfolios = MutableLiveData<List<PortfolioDbModel>>()
    val portfolios: LiveData<List<PortfolioDbModel>>
        get() = _portfolios

    private val portfolioInteractor = PortfolioInteractor()

    fun loadAllPortfolios() = viewModelScope.launch(Dispatchers.IO) {
        _portfolios.postValue(portfolioInteractor.getAllPortfolios())
    }
}