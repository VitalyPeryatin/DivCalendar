package com.infinity_coder.divcalendar.presentation.portfolio.createportfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import kotlinx.coroutines.launch

class CreatePortfolioViewModel : ViewModel() {

    private val portfolioInteractor = PortfolioInteractor()

    fun addPortfolio(portfolioName: String) = viewModelScope.launch {
        portfolioInteractor.addPortfolio(portfolioName)
    }

}