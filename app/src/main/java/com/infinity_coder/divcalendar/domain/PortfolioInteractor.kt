package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.repositories.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class PortfolioInteractor {

    suspend fun addPortfolio(portfolioName: String) {
        PortfolioRepository.addPortfolio(portfolioName)
    }

    fun getAllPortfolios(): Flow<List<PortfolioDbModel>> {
        return PortfolioRepository.getAllPortfolios()
    }

    fun getAllPortfolioNames(): Flow<List<String>> {
        return getAllPortfolios().map { portfolios ->
            portfolios.map { it.name }
        }
    }

    fun setCurrentPortfolio(name: String) {
        PortfolioRepository.setCurrentPortfolio(name)
    }

    fun getCurrentPortfolio(): String {
        return PortfolioRepository.getCurrentPortfolio()
    }

    suspend fun getPortfolioCount(): Int {
        return getAllPortfolios().first().size
    }

    suspend fun deletePortfolio(name: String) {
        PortfolioRepository.deletePortfolio(name)
    }

    suspend fun renamePortfolio(oldName: String, newName: String) {
        PortfolioRepository.renamePortfolio(oldName, newName)
    }
}