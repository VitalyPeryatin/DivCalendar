package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel

object PortfolioRepository {

    private val portfolioDao = DivCalendarDatabase.roomDatabase.portfolioDao

    suspend fun addPortfolio(portfolio: PortfolioDbModel) {
        portfolioDao.insertPortfolio(portfolio)
    }

    suspend fun deletePortfolio(portfolio: PortfolioDbModel) {
        portfolioDao.deletePortfolio(portfolio)
    }

    suspend fun getAllPortfolios(): List<PortfolioDbModel> {
        return portfolioDao.getAllPortfolios()
    }
}