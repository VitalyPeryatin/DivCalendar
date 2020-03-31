package com.infinity_coder.divcalendar.presentation.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.repositories.SecurityRepository
import kotlinx.coroutines.launch

class PortfolioViewModel : ViewModel() {

    fun getSecuritiesLiveData(): LiveData<List<SecurityPackageDbModel>> =
        SecurityRepository.loadAllSecurityPackages()

    fun changeSecurityPackage(securityPackage: SecurityPackageDbModel) = viewModelScope.launch {
        SecurityRepository.changeSecurityPackage(securityPackage)
    }
}