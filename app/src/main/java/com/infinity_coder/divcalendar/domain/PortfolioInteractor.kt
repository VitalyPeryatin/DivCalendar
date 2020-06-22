package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.repositories.PaymentRepository
import com.infinity_coder.divcalendar.data.repositories.PortfolioRepository
import com.infinity_coder.divcalendar.domain._common.convertStingToDate
import com.infinity_coder.divcalendar.domain.models.SortType
import kotlinx.coroutines.flow.*

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
        return PortfolioRepository.getPortfolioWithSecuritiesFlow(getCurrentPortfolioName()).filterNotNull()
    }

    fun getCurrentSortedPortfolioFlow(): Flow<PortfolioDbModel> {
        return PortfolioRepository.getPortfolioWithSecuritiesFlow(getCurrentPortfolioName())
            .filterNotNull()
            .map {
                when (PortfolioRepository.getCurrentSortType()) {
                    SortType.PAYMENT_DATE -> {
                        val currentPortfolioId = PortfolioRepository.getCurrentPortfolioId()
                        val paymentsFromCached = PaymentRepository.getCachedPaymentsThatHaveNotExpired(currentPortfolioId)
                        if (paymentsFromCached.isNotEmpty()) {
                            it.securities = sortSecuritiesByFirstPaymentDate(it.securities, paymentsFromCached)
                        }
                    }
                    SortType.PROFITABILITY -> {
                        it.securities = it.securities.sortedByDescending(SecurityDbModel::yearYield)
                    }
                    SortType.ALPHABETICALLY -> {
                        it.securities = it.securities.sortedBy(SecurityDbModel::name)
                    }
                }
                it
            }
    }

    private fun sortSecuritiesByFirstPaymentDate(securities: List<SecurityDbModel>, payments: List<PaymentDbModel>): List<SecurityDbModel> {
        val preparedPayments = payments
            .sortedBy { convertStingToDate(it.date).time }
            .distinctBy { it.isin }

        return securities.sortedBy { security ->
            val paymentForSpecificSecurity = preparedPayments.find { it.isin == security.isin }
            if (paymentForSpecificSecurity == null)
                Int.MAX_VALUE
            else
                preparedPayments.indexOf(paymentForSpecificSecurity)
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