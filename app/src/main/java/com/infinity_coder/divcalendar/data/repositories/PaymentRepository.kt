package com.infinity_coder.divcalendar.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.*
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import com.infinity_coder.divcalendar.domain.models.Payment
import com.infinity_coder.divcalendar.presentation.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object PaymentRepository {

    private val securityDao = DivCalendarDatabase.roomDatabase.securityDao

    private val divCalendarApi = RetrofitService.divCalendarApi

    private const val PAYMENTS_PREF_NAME = "Payments"
    private const val PREF_SELECTED_YEAR = "selected_year"
    private val paymentsPreferences = App.instance.getSharedPreferences(PAYMENTS_PREF_NAME, Context.MODE_PRIVATE)

    suspend fun getPayments(startDate: String, endDate: String): Flow<List<Payment>> = flow {
        val currentPortfolio = PortfolioRepository.getCurrentPortfolio()
        val securities = securityDao.getSecurityPackagesForPortfolio(currentPortfolio)
        val paymentsFromNetwork = getPaymentsFromNetwork(securities, startDate, endDate)
        val payments = paymentsFromNetwork.map { payment ->
            Payment.from(payment, securities.find { payment.ticker == it.secid }!!)
        }
        emit(payments)
    }

    fun setSelectedYear(selectedYear: String) {
        paymentsPreferences.edit {
            putString(PREF_SELECTED_YEAR, selectedYear)
        }
    }

    fun getSelectedYear(): String {
        return paymentsPreferences.getString(PREF_SELECTED_YEAR, DateFormatter.getCurrentYear())!!
    }

    private suspend fun getPaymentsFromNetwork(securities: List<SecurityPackageDbModel>, startDate: String, endDate: String): List<PaymentNetworkModel.Response> {
        val tickers = securities.map { it.secid }
        val body = PaymentNetworkModel.Request(tickers, startDate, endDate)
        return divCalendarApi.fetchPayments(body)
    }
}