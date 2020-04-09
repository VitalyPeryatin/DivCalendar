package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel

object SecurityRepository {

    private val securityDao = DivCalendarDatabase.roomDatabase.securityDao

    suspend fun deleteSecurityPackage(securityPackage: SecurityPackageDbModel) {
        securityDao.deleteSecurityPackage(securityPackage)
    }

    suspend fun addSecurityPackage(securityPackage: SecurityPackageDbModel) {
        securityDao.insertSecurityPackage(securityPackage)
    }

    suspend fun updateSecurityPackage(securityPackage: SecurityPackageDbModel) {
        securityDao.updateSecurityPackage(securityPackage)
    }

    suspend fun getSecurityPackage(portfolioName: String, secid: String): SecurityPackageDbModel? {
        return securityDao.getSecurityPackage(portfolioName, secid)
    }
}