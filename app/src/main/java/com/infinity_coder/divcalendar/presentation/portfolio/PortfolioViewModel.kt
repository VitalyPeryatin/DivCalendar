package com.infinity_coder.divcalendar.presentation.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.domain.PortfolioInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PortfolioViewModel : ViewModel() {

    private val _state = MutableLiveData(VIEW_STATE_PORTFOLIO_CONTENT)
    val state: LiveData<Int>
        get() = _state

    private val _securities = MutableLiveData<List<SecurityPackageDbModel>>()
    val securities: LiveData<List<SecurityPackageDbModel>>
        get() = _securities

    private val portfolioInteractor = PortfolioInteractor()

    init {
        loadAllSecurityPackages()
            .flowOn(Dispatchers.IO)
            .onEach {
                _securities.value = it
                if (it.isEmpty()) {
                    _state.value = VIEW_STATE_PORTFOLIO_EMPTY
                } else {
                    _state.value = VIEW_STATE_PORTFOLIO_CONTENT
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadAllSecurityPackages(): Flow<List<SecurityPackageDbModel>> =
        portfolioInteractor.loadAllSecurityPackages()

    fun changeSecurityPackage(securityPackage: SecurityPackageDbModel) = viewModelScope.launch {
        portfolioInteractor.changeSecurityPackage(securityPackage)
    }

    companion object {
        const val VIEW_STATE_PORTFOLIO_CONTENT = 1
        const val VIEW_STATE_PORTFOLIO_EMPTY = 2
    }
}