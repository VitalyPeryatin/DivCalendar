package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.repositories.SecurityRepository
import java.math.BigDecimal

class SecurityInteractor {

    private val portfolioInteractor = PortfolioInteractor()

    suspend fun changeSecurityPackage(securityPackage: SecurityDbModel) {
        securityPackage.portfolioId = portfolioInteractor.getCurrentPortfolioId()
        if (securityPackage.count <= BigDecimal.ZERO) {
            SecurityRepository.deleteSecurityPackage(securityPackage)
        } else {
            SecurityRepository.updateSecurityPackage(securityPackage)
        }
    }

    suspend fun deleteSecurityPackage(securityPackage: SecurityDbModel) {
        SecurityRepository.deleteSecurityPackage(securityPackage)
    }

    suspend fun appendSecurityPackage(newSecurityPackage: SecurityDbModel) {
        val security = getSecurityByIsin(newSecurityPackage.isin)
        if (security == null) {
            newSecurityPackage.portfolioId = portfolioInteractor.getCurrentPortfolioId()
            SecurityRepository.addSecurityPackage(newSecurityPackage)
        } else {
            security.count += newSecurityPackage.count
            security.totalPrice += newSecurityPackage.totalPrice
            SecurityRepository.updateSecurityPackage(security)
        }
    }

    suspend fun getSecurityCount(): Int {
        val portfolioId = portfolioInteractor.getCurrentPortfolioId()
        return SecurityRepository.getSecurityCount(portfolioId)
    }

    fun getColorForSecurityLogo(logo: String): Int {
        return SecurityRepository.getColorForSecurityLogo(logo)
    }

    private suspend fun getSecurityByIsin(isin: String): SecurityDbModel? {
        val portfolioId = portfolioInteractor.getCurrentPortfolioId()
        return SecurityRepository.getSecurity(portfolioId, isin)
    }
}