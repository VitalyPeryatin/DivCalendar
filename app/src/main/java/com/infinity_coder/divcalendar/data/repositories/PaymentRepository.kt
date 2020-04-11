package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.*
import com.infinity_coder.divcalendar.domain.models.Payment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

object PaymentRepository {

    private val divCalendarApi = RetrofitService.divCalendarApi

    suspend fun getPayments(startDate: String, endDate: String): Flow<List<Payment>> = flow {
        val paymentsFromNetwork = getPaymentsFromNetwork(startDate, endDate)
        val securities = getSecuritiesForCurrentPortfolio()
        val payments = paymentsFromNetwork.map {payment ->
            Payment.from(payment, securities.find { payment.ticker == it.secid }!!)
        }
        emit(payments)
    }

    private suspend fun getPaymentsFromNetwork(startDate: String, endDate: String): List<PaymentNetworkModel> {
        val securities = getSecuritiesForCurrentPortfolio().map(SecurityPackageDbModel::secid)
        val body = BodyPaymentNetworkModel(securities, startDate, endDate)
        return divCalendarApi.fetchPayments(body)
    }

    private suspend fun getSecuritiesForCurrentPortfolio(): List<SecurityPackageDbModel> {
        val currentPortfolioName = PortfolioRepository.getCurrentPortfolio()
        return PortfolioRepository.getPortfolioWithSecurities(currentPortfolioName)
            .first().securities
    }
}