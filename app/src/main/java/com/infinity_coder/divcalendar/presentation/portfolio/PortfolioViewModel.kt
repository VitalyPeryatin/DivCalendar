package com.infinity_coder.divcalendar.presentation.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.data.db.model.SecPackageDbModel
import com.infinity_coder.divcalendar.data.repositories.SecRepository

class PortfolioViewModel : ViewModel() {

    fun getStocksLiveData(): LiveData<List<SecPackageDbModel>> =
        SecRepository.loadAllStockPackages()

}