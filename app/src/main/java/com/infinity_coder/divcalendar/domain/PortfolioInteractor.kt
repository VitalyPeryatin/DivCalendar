package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.repositories.PortfolioRepository
import kotlinx.coroutines.flow.Flow

class PortfolioInteractor {

    fun loadAllSecurityPackages(): Flow<List<SecurityPackageDbModel>> {
        return PortfolioRepository.getAllSecurityPackages()
    }

    suspend fun changeSecurityPackage(securityPackage: SecurityPackageDbModel) {
        if (securityPackage.count <= 0) {
            PortfolioRepository.deleteSecurityPackage(securityPackage)
        } else {
            PortfolioRepository.addSecurityPackage(securityPackage)
        }
    }

    suspend fun appendSecurityPackage(newSecurityPackage: SecurityPackageDbModel) {
        var securitiesPackage = PortfolioRepository.getSecurityPackage(newSecurityPackage.secid)
        if (securitiesPackage == null) {
            securitiesPackage = newSecurityPackage
        } else {
            securitiesPackage.count += newSecurityPackage.count
            securitiesPackage.totalPrice += newSecurityPackage.totalPrice
        }
        PortfolioRepository.addSecurityPackage(securitiesPackage)
    }
}