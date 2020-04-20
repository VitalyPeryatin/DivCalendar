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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ChangePortfolioViewModel : ViewModel() {

    private val _portfolios = MutableLiveData<List<PortfolioDbModel>>()
    val portfolios: LiveData<List<PortfolioDbModel>>
        get() = _portfolios

    val showDeletePortfolioDialogEvent = LiveEvent<PortfolioDbModel>()
    val hideDeletePortfolioDialogEvent = LiveEvent<Unit?>()
    val errorMessageEvent = LiveEvent<Int>()
    val currentPortfolioEvent = LiveEvent<String>()
    val renamePortfolioEvent = LiveEvent<String>()
    val createPortfolioEvent = LiveEvent<Unit?>()

    private val portfolioInteractor = PortfolioInteractor()

    fun loadAllPortfolios() = viewModelScope.launch(Dispatchers.IO) {
        portfolioInteractor.getAllPortfolios()
            .onEach { _portfolios.value = it }
            .launchIn(viewModelScope)
    }

    fun requestRenamePortfolio(oldName: String, newName: String) = viewModelScope.launch {
        val portfolioNames = portfolioInteractor.getAllPortfolioNames()
        when {
            newName.isBlank() -> {
                errorMessageEvent.value = ERROR_CODE_EMPTY_PORTFOLIO_NAME
            }
            portfolioNames.contains(newName) -> {
                errorMessageEvent.value = ERROR_CODE_NOT_UNIQUE_NAME
            }
            else -> {
                portfolioInteractor.renamePortfolio(oldName, newName)
                renamePortfolioEvent.value = newName
            }
        }
    }

    fun setCurrentPortfolio(name: String) {
        portfolioInteractor.setCurrentPortfolio(name)
    }

    fun requestCreatePortfolio(portfolioName: String) = viewModelScope.launch {
        val portfolioNames = portfolioInteractor.getAllPortfolioNames()
        when {
            portfolioName.isBlank() -> {
                errorMessageEvent.value = ERROR_CODE_EMPTY_PORTFOLIO_NAME
            }
            portfolioNames.contains(portfolioName) -> {
                errorMessageEvent.value = ERROR_CODE_NOT_UNIQUE_NAME
            }
            else -> {
                portfolioInteractor.addPortfolio(portfolioName)
                createPortfolioEvent.value = null
                currentPortfolioEvent.value = portfolioName
            }
        }
    }

    fun requestConfirmationOnDeletePortfolio(portfolio: PortfolioDbModel) = viewModelScope.launch {
        if (portfolioInteractor.getPortfolioCount() > 1) {
            showDeletePortfolioDialogEvent.value = portfolio
        } else {
            errorMessageEvent.value = ERROR_CODE_SMALL_COUNT_PORTFOLIO
        }
    }

    fun deletePortfolio(name: String) = viewModelScope.launch {
        val isCheckout = checkoutCurrentPortfolio(name)
        if (isCheckout) {
            portfolioInteractor.deletePortfolio(name)
            hideDeletePortfolioDialogEvent.value = null
        }
    }

    private suspend fun checkoutCurrentPortfolio(name: String): Boolean {
        val currentPortfolioName = portfolioInteractor.getCurrentPortfolioName()
        if (currentPortfolioName == name) {
            val portfolioNames = portfolioInteractor.getAllPortfolioNames()
            val deleteNameIndex = portfolioNames.indexOf(name)

            val nextPortfolioName = when {

                deleteNameIndex + 1 < portfolioNames.size -> portfolioNames[deleteNameIndex + 1]

                deleteNameIndex - 1 >= 0 -> portfolioNames[deleteNameIndex - 1]

                else -> return false
            }
            currentPortfolioEvent.value = nextPortfolioName
        }
        return true
    }

    companion object {
        const val ERROR_CODE_SMALL_COUNT_PORTFOLIO = 1
        const val ERROR_CODE_EMPTY_PORTFOLIO_NAME = 2
        const val ERROR_CODE_NOT_UNIQUE_NAME = 3
    }
}