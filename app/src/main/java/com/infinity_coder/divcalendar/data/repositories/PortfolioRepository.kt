package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object PortfolioRepository {

    private val portfolioDao = DivCalendarDatabase.roomDatabase.portfolioDao

    fun getAllSecurityPackages(): Flow<List<SecurityPackageDbModel>> = flow {
        emit(portfolioDao.getSecurityPackages())
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