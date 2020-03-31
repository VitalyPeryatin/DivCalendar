package com.infinity_coder.divcalendar.data.repositories

import androidx.lifecycle.LiveData
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.ShortSecurityNetworkModel

object SecurityRepository {

    private val securityDao = DivCalendarDatabase.roomDatabase.securityDao
    private val moexApi = RetrofitService.moexApi

    suspend fun loadAllSecurities(): List<ShortSecurityNetworkModel> {
        return moexApi.getSecurities().securities
    }

    fun loadAllSecurityPackages(): LiveData<List<SecurityPackageDbModel>> {
        return securityDao.getAllSecuritiesPackageLiveData()
    }

    suspend fun changeSecurityPackage(securityPackage: SecurityPackageDbModel) {
        if (securityPackage.count <= 0) {
            securityDao.deleteSecurityPackage(securityPackage)
        } else {
            securityDao.addSecurityPackage(securityPackage)
        }
    }

    suspend fun appendSecurityPackage(newSecurityPackage: SecurityPackageDbModel) {
        var securitiesPackage = securityDao.getSecurityPackage(newSecurityPackage.secid)
        if (securitiesPackage == null) {
            securitiesPackage = newSecurityPackage
        } else {
            securitiesPackage.count += newSecurityPackage.count
            securitiesPackage.totalPrice += newSecurityPackage.totalPrice
        }
        securityDao.addSecurityPackage(securitiesPackage)
    }
}