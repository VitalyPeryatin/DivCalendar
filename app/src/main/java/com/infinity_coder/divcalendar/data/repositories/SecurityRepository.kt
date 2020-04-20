package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel

object SecurityRepository {

    private val securityDao = DivCalendarDatabase.roomDatabase.securityDao

    suspend fun deleteSecurityPackage(securityPackage: SecurityDbModel) {
        securityDao.deleteSecurityPackage(securityPackage)
    }

    suspend fun addSecurityPackage(securityPackage: SecurityDbModel) {
        securityDao.insertSecurityPackage(securityPackage)
    }

    suspend fun updateSecurityPackage(securityPackage: SecurityDbModel) {
        securityDao.updateSecurityPackage(securityPackage)
    }

    suspend fun getSecurityPackage(portfolioId: Long, secid: String): SecurityDbModel? {
        return securityDao.getSecurityPackage(portfolioId, secid)
    }

    suspend fun getSecurityCount(portfolioId: Long): Int {
        return securityDao.getSecurityCountForPortfolio(portfolioId)
    }
}