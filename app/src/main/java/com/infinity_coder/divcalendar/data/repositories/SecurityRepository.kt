package com.infinity_coder.divcalendar.data.repositories

import androidx.lifecycle.LiveData
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.ShortSecNetworkModel

object SecurityRepository {

    private val securityDao = DivCalendarDatabase.roomDatabase.securityDao
    private val moexApi = RetrofitService.moexApi

    suspend fun loadAllSecurities(): List<ShortSecNetworkModel> {
        return moexApi.getSecurities().securities
    }

    fun loadAllSecsPackages(): LiveData<List<SecurityPackageDbModel>> {
        return securityDao.getAllSecuritiesPackageLiveData()
    }

    suspend fun appendSecurityPackage(newSecPackage: SecurityPackageDbModel) {
        var securitiesPackage = securityDao.getSecurityPackage(newSecPackage.secid)
        if (securitiesPackage == null) {
            securitiesPackage = newSecPackage
        } else {
            securitiesPackage.count += newSecPackage.count
            securitiesPackage.totalPrice += newSecPackage.totalPrice
        }
        securityDao.addSecurityPackage(securitiesPackage)
    }
}