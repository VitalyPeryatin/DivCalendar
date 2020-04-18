package com.infinity_coder.divcalendar.presentation.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.PortfolioWithSecurities
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import com.infinity_coder.divcalendar.domain.SecurityInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PortfolioViewModel : ViewModel() {

    private val _state = MutableLiveData<Int>()
    val state: LiveData<Int>
        get() = _state

    private val _portfolio = MutableLiveData<PortfolioWithSecurities>()
    val portfolio: LiveData<PortfolioWithSecurities>
        get() = _portfolio

    private val securityInteractor = SecurityInteractor()
    private val portfolioInteractor = PortfolioInteractor()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadSecurities() = viewModelScope.launch {
        portfolioInteractor.getCurrentPortfolio()
            .onEach {
                _portfolio.value = it
                if (it.securities.isEmpty()) {
                    _state.value = VIEW_STATE_PORTFOLIO_EMPTY
                } else {
                    _state.value = VIEW_STATE_PORTFOLIO_CONTENT
                }
            }.launchIn(viewModelScope)
    }

    fun changeSecurityPackage(securityPackage: SecurityPackageDbModel) = viewModelScope.launch {
        securityInteractor.changeSecurityPackage(securityPackage)
    }

    companion object {
        const val VIEW_STATE_PORTFOLIO_CONTENT = 1
        const val VIEW_STATE_PORTFOLIO_EMPTY = 2
    }
}