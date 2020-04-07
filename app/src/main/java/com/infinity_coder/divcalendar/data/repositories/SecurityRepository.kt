package com.infinity_coder.divcalendar.data.repositories

import androidx.lifecycle.LiveData
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel

object SecurityRepository {

    private val securityDao = DivCalendarDatabase.roomDatabase.securityDao

    fun loadAllSecurityPackages(): LiveData<List<SecurityPackageDbModel>> {
        return securityDao.getAllSecuritiesPackageLiveData()
    }

    suspend fun deleteSecurityPackage(securityPackage: SecurityPackageDbModel) {
        securityDao.deleteSecurityPackage(securityPackage)
    }

    suspend fun addSecurityPackage(securityPackage: SecurityPackageDbModel) {
        securityDao.addSecurityPackage(securityPackage)
    }

    suspend fun getSecurityPackage(secid: String): SecurityPackageDbModel? {
        return securityDao.getSecurityPackage(secid)
    }
}