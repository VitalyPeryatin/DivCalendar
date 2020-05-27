package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
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

    suspend fun getAllPortfoliosWithSecurities(): List<PortfolioDbModel> {
        return PortfolioRepository.getAllPortfoliosWithSecurity()
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
        return getCurrentPortfolioFlow().first().securities.isEmpty()
    }

    suspend fun addDefaultPortfolio() {
        if (getPortfolioCount() == 0) {
            val securityInteractor = SecurityInteractor()
            addPortfolio(DEFAULT_PORTFOLIO_NAME)
            setCurrentPortfolio(DEFAULT_PORTFOLIO_NAME)
            securityInteractor.appendSecurityPackage(LSRG_SECURITY_DB_MODEL)
            securityInteractor.appendSecurityPackage(MTS_SECURITY_DB_MODEL)
            securityInteractor.appendSecurityPackage(NLMK_SECURITY_DB_MODEL)
        }
    }

    companion object {
        const val DEFAULT_PORTFOLIO_NAME = "Основной портфель"

        val LSRG_SECURITY_DB_MODEL = SecurityDbModel(
            isin = "RU000A0JPFP0",
            ticker = "LSRG",
            name = "Группа ЛСР",
            logo = "https://static.tinkoff.ru/brands/traiding/RU000A0JPFP0x160.png",
            exchange = "MOEX",
            count = 10,
            totalPrice = 5914.0,
            yearYield = 13.26f,
            currency = "RUB",
            type = "stock",
            color = -2090944
        )

        val MTS_SECURITY_DB_MODEL = SecurityDbModel(
            isin = "RU0007775219",
            ticker = "MTSS",
            name = "МТС",
            logo = "https://static.tinkoff.ru/brands/traiding/RU0007775219x160.png",
            exchange = "MOEX",
            count = 20,
            totalPrice = 6638.0,
            yearYield = 8.72f,
            currency = "RUB",
            type = "stock",
            color = -2097136
        )

        val NLMK_SECURITY_DB_MODEL = SecurityDbModel(
            isin = "RU0009046452",
            ticker = "NLMK",
            name = "НЛМК",
            logo = "https://static.tinkoff.ru/brands/traiding/RU0009046452x160.png",
            exchange = "MOEX",
            count = 40,
            totalPrice = 5412.0,
            yearYield = 10.32f,
            currency = "RUB",
            type = "stock",
            color = -16232288
        )
    }
}