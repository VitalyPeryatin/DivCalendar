package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.repositories.SecurityRepository

class SecurityInteractor {

    private val portfolioInteractor = PortfolioInteractor()

    suspend fun changeSecurityPackage(securityPackage: SecurityPackageDbModel) {
        securityPackage.portfolioName = getCurrentPortfolioName()
        if (securityPackage.count <= 0) {
            SecurityRepository.deleteSecurityPackage(securityPackage)
        } else {
            SecurityRepository.updateSecurityPackage(securityPackage)
        }
    }

    suspend fun appendSecurityPackage(newSecurityPackage: SecurityPackageDbModel) {
        val portfolioName = getCurrentPortfolioName()
        val securitiesPackage = SecurityRepository.getSecurityPackage(portfolioName, newSecurityPackage.secid)
        if (securitiesPackage == null) {
            newSecurityPackage.portfolioName = portfolioName
            SecurityRepository.addSecurityPackage(newSecurityPackage)
        } else {
            securitiesPackage.count += newSecurityPackage.count
            securitiesPackage.totalPrice += newSecurityPackage.totalPrice
            SecurityRepository.updateSecurityPackage(securitiesPackage)
        }
    }

    suspend fun getSecurityCount(): Int {
        val portfolioName = getCurrentPortfolioName()
        return SecurityRepository.getSecurityCount(portfolioName)
    }

    private fun getCurrentPortfolioName(): String {
        return portfolioInteractor.getCurrentPortfolioName()
    }
}