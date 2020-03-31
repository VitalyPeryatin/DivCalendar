package com.infinity_coder.divcalendar.presentation.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.repositories.SecurityRepository

class PortfolioViewModel : ViewModel() {

    fun getSecsLiveData(): LiveData<List<SecurityPackageDbModel>> =
        SecurityRepository.loadAllSecsPackages()

}