package com.infinity_coder.divcalendar.presentation.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import com.infinity_coder.divcalendar.domain.RateInteractor
import com.infinity_coder.divcalendar.domain.SecurityInteractor
import com.infinity_coder.divcalendar.presentation._common.extensions.sumByFloat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PortfolioViewModel : ViewModel() {

    private val _state = MutableLiveData<Int>()
    val state: LiveData<Int>
        get() = _state

    private val _portfolio = MutableLiveData<PortfolioDbModel>()
    val portfolio: LiveData<PortfolioDbModel>
        get() = _portfolio

    private val _totalPortfolioCost = MutableLiveData<Double>()
    val totalPortfolioCost: LiveData<Double>
        get() = _totalPortfolioCost

    private val securityInteractor = SecurityInteractor()
    private val portfolioInteractor = PortfolioInteractor()
    private val rateInteractor = RateInteractor()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadSecurities() = viewModelScope.launch {
        portfolioInteractor.getCurrentPortfolio()
            .onEach {
                _portfolio.value = it
                calculateTotalPortfolioCost(it)
                if (it.securities.isEmpty()) {
                    _state.value = VIEW_STATE_PORTFOLIO_EMPTY
                } else {
                    _state.value = VIEW_STATE_PORTFOLIO_CONTENT
                }
            }.launchIn(viewModelScope)
    }

    fun changeSecurityPackage(securityPackage: SecurityDbModel) = viewModelScope.launch {
        securityInteractor.changeSecurityPackage(securityPackage)
        loadSecurities()
    }

    fun getDisplayCurrency(): String {
        return rateInteractor.getDisplayCurrency()
    }

    fun setDisplayCurrency(currency: String) = viewModelScope.launch {
        rateInteractor.saveDisplayCurrency(currency)
        if (_portfolio.value != null)
            calculateTotalPortfolioCost(_portfolio.value!!)
    }

    private suspend fun calculateTotalPortfolioCost(portfolio: PortfolioDbModel) {
        val currentCurrency = rateInteractor.getDisplayCurrency()
        val totalCost = portfolio.securities.sumByDouble {
            rateInteractor.convertCurrencies(it.totalPrice, it.currency, currentCurrency)
        }
        _totalPortfolioCost.value = totalCost
    }

    companion object {
        const val VIEW_STATE_PORTFOLIO_CONTENT = 1
        const val VIEW_STATE_PORTFOLIO_EMPTY = 2
    }
}