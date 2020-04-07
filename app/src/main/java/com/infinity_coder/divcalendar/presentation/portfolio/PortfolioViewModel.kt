package com.infinity_coder.divcalendar.presentation.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.domain.SecurityInteractor
import kotlinx.coroutines.launch

class PortfolioViewModel : ViewModel() {

    private val portfolioInteractor = SecurityInteractor()

    fun getSecuritiesLiveData(): LiveData<List<SecurityPackageDbModel>> =
        portfolioInteractor.loadAllSecurityPackages()

    fun changeSecurityPackage(securityPackage: SecurityPackageDbModel) = viewModelScope.launch {
        portfolioInteractor.changeSecurityPackage(securityPackage)
    }
}