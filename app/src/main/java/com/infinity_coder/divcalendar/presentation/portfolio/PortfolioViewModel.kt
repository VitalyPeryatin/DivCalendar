package com.infinity_coder.divcalendar.presentation.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import com.infinity_coder.divcalendar.domain.RateInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PortfolioViewModel : ViewModel() {

    private val _portfolio = MutableLiveData<PortfolioDbModel>()
    val portfolio: LiveData<PortfolioDbModel>
        get() = _portfolio

    private val _state = MutableLiveData<Int>()
    val state: LiveData<Int>
        get() = _state

    private val _totalPortfolioCost = MutableLiveData<Double>()
    val totalPortfolioCost: LiveData<Double>
        get() = _totalPortfolioCost

    private val portfolioInteractor = PortfolioInteractor()
    private val rateInteractor = RateInteractor()

    private var portfolioJob: Job? = null

    init {
        loadSecurities()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadSecurities() = viewModelScope.launch {
        portfolioJob?.cancel()
        portfolioJob = portfolioInteractor.getCurrentSortedPortfolioFlow()
            .onEach {
                _portfolio.value = it
                calculateTotalPortfolioCost(it)
                if (it.securities.isEmpty()) {
                    _state.value = VIEW_STATE_PORTFOLIO_EMPTY
                } else {
                    _state.value = VIEW_STATE_PORTFOLIO_CONTENT
                }
            }
            .catch { }
            .launchIn(viewModelScope)
    }

    fun getDisplayCurrency(): String {
        return rateInteractor.getDisplayCurrency()
    }

    fun setDisplayCurrency(currency: String) = viewModelScope.launch {
        rateInteractor.saveDisplayCurrency(currency)
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
        const val VIEW_STATE_PORTFOLIO_CONTENT = 2
        const val VIEW_STATE_PORTFOLIO_EMPTY = 3
    }
}