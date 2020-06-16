package com.infinity_coder.divcalendar.presentation.portfolio.sorting

import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import com.infinity_coder.divcalendar.domain.models.SortType
import com.infinity_coder.divcalendar.presentation._common.LiveEvent

class SortingPortfolioViewModel : ViewModel() {

    val changeSortType = LiveEvent<Void?>()

    private val portfolioInteractor = PortfolioInteractor()

    fun getCurrentSortType(): SortType {
        return portfolioInteractor.getCurrentSortType()
    }

    fun setCurrentSortType(sortType: SortType) {
        changeSortType.value = null
        portfolioInteractor.setCurrentSortType(sortType)
    }
}