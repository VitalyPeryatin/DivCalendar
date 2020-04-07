package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.repositories.PortfolioRepository


class PortfolioInteractor {

    suspend fun addPortfolio(portfolioName: String) {
        val portfolio = PortfolioDbModel(portfolioName)
        PortfolioRepository.addPortfolio(portfolio)
    }

    suspend fun deletePortfolio(portfolioName: String) {
        val portfolio = PortfolioDbModel(portfolioName)
        PortfolioRepository.deletePortfolio(portfolio)
    }

    suspend fun getAllPortfolios(): List<PortfolioDbModel> {
        return PortfolioRepository.getAllPortfolios()
    }
}