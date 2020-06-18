package com.infinity_coder.divcalendar.domain

import java.math.BigDecimal

class SubscriptionInteractor {

    private val securityInteractor = SecurityInteractor()
    private val portfolioInteractor = PortfolioInteractor()

    suspend fun hasPremiumProducts(): Boolean {
        val portfolioCount = portfolioInteractor.getPortfolioCount()
        val securityCount = securityInteractor.getSecurityCount()
        return securityCount > MAX_SECURITY_COUNT_WITHOUT_SUBSCRIPTION || portfolioCount > MAX_PORTFOLIO_COUNT_WITHOUT_SUBSCRIPTION
    }

    suspend fun isSecurityCountLeastThanSubscriptionGrant(): Boolean {
        return securityInteractor.getSecurityCount() < MAX_SECURITY_COUNT_WITHOUT_SUBSCRIPTION
    }

    companion object {
        private val MAX_SECURITY_COUNT_WITHOUT_SUBSCRIPTION = BigDecimal(5)
        private const val MAX_PORTFOLIO_COUNT_WITHOUT_SUBSCRIPTION = 1
    }
}