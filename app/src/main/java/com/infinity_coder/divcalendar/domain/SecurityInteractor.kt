package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.repositories.SecurityRepository

class SecurityInteractor {

    private val portfolioInteractor = PortfolioInteractor()

    suspend fun changeSecurityPackage(securityPackage: SecurityDbModel) {
        securityPackage.portfolioId = portfolioInteractor.getCurrentPortfolioId()
        if (securityPackage.count <= 0) {
            SecurityRepository.deleteSecurityPackage(securityPackage)
        } else {
            SecurityRepository.updateSecurityPackage(securityPackage)
        }
    }

    suspend fun appendSecurityPackage(newSecurityPackage: SecurityDbModel) {
        val portfolioId = portfolioInteractor.getCurrentPortfolioId()
        val securitiesPackage = SecurityRepository.getSecurityPackage(portfolioId, newSecurityPackage.ticker)
        if (securitiesPackage == null) {
            newSecurityPackage.portfolioId = portfolioId
            SecurityRepository.addSecurityPackage(newSecurityPackage)
        } else {
            securitiesPackage.count += newSecurityPackage.count
            securitiesPackage.totalPrice += newSecurityPackage.totalPrice
            SecurityRepository.updateSecurityPackage(securitiesPackage)
        }
    }

    suspend fun getSecurityCount(): Int {
        val portfolioId = portfolioInteractor.getCurrentPortfolioId()
        return SecurityRepository.getSecurityCount(portfolioId)
    }

    fun getColorForSecurityLogo(logo: String): Int {
        return SecurityRepository.getColorForSecurityLogo(logo)
    }
}