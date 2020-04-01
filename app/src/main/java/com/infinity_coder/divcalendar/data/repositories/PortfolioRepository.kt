package com.infinity_coder.divcalendar.data.repositories

import androidx.lifecycle.LiveData
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel

object PortfolioRepository {

    private val portfolioDao = DivCalendarDatabase.roomDatabase.portfolioDao

    fun loadAllSecurityPackages(): LiveData<List<SecurityPackageDbModel>> {
        return portfolioDao.getAllSecuritiesPackageLiveData()
    }

    suspend fun deleteSecurityPackage(securityPackage: SecurityPackageDbModel) {
        portfolioDao.deleteSecurityPackage(securityPackage)
    }

    suspend fun addSecurityPackage(securityPackage: SecurityPackageDbModel) {
        portfolioDao.addSecurityPackage(securityPackage)
    }

    suspend fun getSecurityPackage(secid: String): SecurityPackageDbModel? {
        return portfolioDao.getSecurityPackage(secid)
    }
}