package com.infinity_coder.divcalendar.presentation.portfolio.sorting

import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import com.infinity_coder.divcalendar.domain.models.SortType

class SortingPortfolioViewModel : ViewModel() {

    private val portfolioInteractor = PortfolioInteractor()

    private val firstSortType: SortType = portfolioInteractor.getCurrentSortType()

    fun getCurrentSortType(): SortType {
        return portfolioInteractor.getCurrentSortType()
    }

    fun setCurrentSortType(sortType: SortType) {
        portfolioInteractor.setCurrentSortType(sortType)
    }

    fun isUpdatePortfolio(): Boolean {
        return firstSortType != portfolioInteractor.getCurrentSortType()
    }
}