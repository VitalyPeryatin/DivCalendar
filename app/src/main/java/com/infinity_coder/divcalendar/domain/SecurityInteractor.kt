package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.PortfolioWithSecurities
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.repositories.SecurityRepository
import kotlinx.coroutines.flow.Flow

class SecurityInteractor {

    fun loadAllSecurityPackages(portfolioName: String): Flow<PortfolioWithSecurities?> {
        return SecurityRepository.loadPortfolio(portfolioName)
    }

    suspend fun changeSecurityPackage(portfolioName: String, securityPackage: SecurityPackageDbModel) {
        if (securityPackage.count <= 0) {
            SecurityRepository.deleteSecurityPackage(securityPackage)
        } else {
            addSecurityPackage(portfolioName, securityPackage)
        }
    }

    suspend fun appendSecurityPackage(portfolioName: String, newSecurityPackage: SecurityPackageDbModel) {
        var securitiesPackage = SecurityRepository.getSecurityPackage(newSecurityPackage.secid)
        if (securitiesPackage == null) {
            securitiesPackage = newSecurityPackage
        } else {
            securitiesPackage.count += newSecurityPackage.count
            securitiesPackage.totalPrice += newSecurityPackage.totalPrice
        }
        addSecurityPackage(portfolioName, securitiesPackage)
    }

    private suspend fun addSecurityPackage(portfolioName: String, packageDbModel: SecurityPackageDbModel) {
        packageDbModel.portfolioName = portfolioName
        SecurityRepository.addSecurityPackage(packageDbModel)
    }
}