package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PortfolioWithSecurities
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import kotlinx.coroutines.flow.Flow

object SecurityRepository {

    private val portfolioDao = DivCalendarDatabase.roomDatabase.portfolioDao

    fun loadPortfolio(portfolioName: String): Flow<PortfolioWithSecurities> =
        portfolioDao.getPortfolioWithSecurities(portfolioName)

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