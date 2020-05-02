package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.repositories.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PortfolioInteractor {

    suspend fun addPortfolio(portfolioName: String) {
        PortfolioRepository.addPortfolio(portfolioName)
    }

    fun getAllPortfolios(): Flow<List<PortfolioDbModel>> {
        return PortfolioRepository.getAllPortfolios()
    }

    suspend fun getAllPortfolioNames(): List<String> {
        return PortfolioRepository.getAllPortfolioNames()
    }

    fun setCurrentPortfolio(name: String) {
        PortfolioRepository.setCurrentPortfolio(name)
    }

    fun getCurrentPortfolioName(): String {
        return PortfolioRepository.getCurrentPortfolioName()
    }

    suspend fun getCurrentPortfolioId(): Long {
        return PortfolioRepository.getCurrentPortfolioId()
    }

    suspend fun getPortfolioCount(): Int {
        return PortfolioRepository.getPortfolioCount()
    }

    suspend fun deletePortfolio(name: String) {
        PortfolioRepository.deletePortfolio(name)
    }

    suspend fun renamePortfolio(oldName: String, newName: String) {
        PortfolioRepository.renamePortfolio(oldName, newName)
        if (PortfolioRepository.getCurrentPortfolioName() == oldName) {
            PortfolioRepository.setCurrentPortfolio(newName)
        }
    }

    fun getCurrentPortfolioFlow(): Flow<PortfolioDbModel> {
        return PortfolioRepository.getPortfolioWithSecurities(getCurrentPortfolioName())
            .filterNotNull()
            .map {
                it.securities = it.securities.sortedBy { security -> security.name }
                it
            }
    }

    suspend fun isCurrentPortfolioEmpty(): Boolean {
        return getCurrentPortfolioFlow().first()
            .securities.isEmpty()
    }
}