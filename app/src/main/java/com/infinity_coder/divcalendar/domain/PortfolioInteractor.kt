package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.network.model.PaymentNetModel
import com.infinity_coder.divcalendar.data.repositories.PaymentRepository
import com.infinity_coder.divcalendar.data.repositories.PortfolioRepository
import com.infinity_coder.divcalendar.domain._common.convertStingToDate
import com.infinity_coder.divcalendar.domain._common.isExpiredDate
import com.infinity_coder.divcalendar.domain.models.SortType
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
                it.securities = it.securities.sortedBy(SecurityDbModel::name)
                it
            }
    }

    fun getCurrentSortingPortfolioFlow(): Flow<PortfolioDbModel> {
        return PortfolioRepository.getPortfolioWithSecurities(getCurrentPortfolioName())
            .filterNotNull()
            .map {
                val sortType = PortfolioRepository.getCurrentSortType()
                it.securities = sortSecuritiesInPortfolio(it.securities, sortType)
                it
            }
    }

    private suspend fun sortSecuritiesInPortfolio(securities: List<SecurityDbModel>, sortType: SortType): List<SecurityDbModel> {
        return when (sortType) {
            is SortType.PaymentDate -> {
                val payments = PaymentRepository.getPaymentForCurrentYear()
                    .sortedBy { convertStingToDate(it.date).time }
                    .filterNot { isExpiredDate(it.date) }
                    .distinctBy(PaymentNetModel.Response ::isin)

                securities.sortedBy { security ->
                    val payment = payments.find { it.isin == security.isin }
                    if (payment == null)
                        Int.MAX_VALUE
                    else
                        payments.indexOf(payment)
                }
            }
            is SortType.Profitability -> {
                securities.sortedByDescending(SecurityDbModel::yearYield)
            }
            is SortType.Alphabetically -> {
                securities.sortedBy(SecurityDbModel::name)
            }
        }
    }

    suspend fun isCurrentPortfolioEmpty(): Boolean {
        return getCurrentPortfolioFlow().first().securities.isEmpty()
    }

    fun setCurrentSortType(sortType: SortType) {
        PortfolioRepository.setCurrentSortType(sortType)
    }

    fun getCurrentSortType(): SortType {
        return PortfolioRepository.getCurrentSortType()
    }
}