package com.infinity_coder.divcalendar.presentation.portfolio.manageportfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import com.infinity_coder.divcalendar.presentation._common.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ChangePortfolioViewModel : ViewModel() {

    private val _portfolios = MutableLiveData<List<PortfolioDbModel>>()
    val portfolios: LiveData<List<PortfolioDbModel>>
        get() = _portfolios

    val showDeletePortfolioDialogEvent = LiveEvent<PortfolioDbModel>()
    val errorMessageEvent = LiveEvent<Int>()
    val currentPortfolioEvent = LiveEvent<String>()

    private val portfolioInteractor = PortfolioInteractor()

    fun loadAllPortfolios() = viewModelScope.launch(Dispatchers.IO) {
        portfolioInteractor.getAllPortfolios()
            .onEach { _portfolios.value = it }
            .launchIn(viewModelScope)
    }

    fun setCurrentPortfolio(name: String) {
        portfolioInteractor.setCurrentPortfolio(name)
    }

    fun addPortfolio(portfolioName: String) = viewModelScope.launch {
        portfolioInteractor.addPortfolio(portfolioName)
    }

    fun tryDeletePortfolio(portfolio: PortfolioDbModel) = viewModelScope.launch {
        if (portfolioInteractor.getPortfolioCount() > 1) {
            showDeletePortfolioDialogEvent.value = portfolio
        } else {
            errorMessageEvent.value = ERROR_MESSAGE_SMALL_COUNT_PORTFOLIO
        }
    }

    fun renamePortfolio(oldName: String, newName: String) = viewModelScope.launch {
        portfolioInteractor.renamePortfolio(oldName, newName)
    }

    fun deletePortfolio(name: String) = viewModelScope.launch {
        val isCheckout = checkoutCurrentPortfolio(name)
        if (isCheckout) {
            portfolioInteractor.deletePortfolio(name)
        }
    }

    private suspend fun checkoutCurrentPortfolio(name: String): Boolean {
        val currentPortfolioName = portfolioInteractor.getCurrentPortfolioName()
        if (currentPortfolioName == name) {
            val portfolioNames = portfolioInteractor.getAllPortfolioNames().first()
            val deleteNameIndex = portfolioNames.indexOf(name)
            val iterator = portfolioNames.listIterator(deleteNameIndex)
            val nextPortfolioName = when {

                iterator.hasNext() -> iterator.next()

                iterator.hasPrevious() -> iterator.previous()

                else -> return false
            }
            currentPortfolioEvent.value = nextPortfolioName
        }
        return true
    }

    companion object {
        const val ERROR_MESSAGE_SMALL_COUNT_PORTFOLIO = 1
    }
}