package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.repositories.PortfolioRepository


class PortfolioInteractor {

    suspend fun addPortfolio(portfolioName: String) {
        PortfolioRepository.addPortfolio(portfolioName)
    }

    suspend fun getAllPortfolios(): List<PortfolioDbModel> {
        return PortfolioRepository.getAllPortfolios()
    }

    fun setCurrentPortfolio(name: String) {
        PortfolioRepository.setCurrentPortfolio(name)
    }

    fun getCurrentPortfolio(): String {
        return PortfolioRepository.getCurrentPortfolio()
    }
}