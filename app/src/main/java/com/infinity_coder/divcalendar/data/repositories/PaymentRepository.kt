package com.infinity_coder.divcalendar.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.PaymentNetModel
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import com.infinity_coder.divcalendar.presentation.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object PaymentRepository {

    private val securityDao = DivCalendarDatabase.roomDatabase.securityDao
    private val paymentDao = DivCalendarDatabase.roomDatabase.paymentDao

    private val divCalendarApi = RetrofitService.divCalendarApi

    private const val PAYMENTS_PREF_NAME = "Payments"
    private const val PREF_SELECTED_YEAR = "selected_year"
    private val paymentsPreferences = App.instance.getSharedPreferences(PAYMENTS_PREF_NAME, Context.MODE_PRIVATE)

    suspend fun getPayments(startDate: String, endDate: String): Flow<List<PaymentDbModel>> = flow {
        val currentPortfolioId = PortfolioRepository.getCurrentPortfolioId()

        val cachedPayments = paymentDao.getPaymentsWithSecurity(currentPortfolioId, startDate, endDate)
        emit(cachedPayments)

        val updatedPayments = getPaymentsFromNetworkAndSaveToDb(currentPortfolioId, startDate, endDate)
        emit(updatedPayments)
    }

    private suspend fun getPaymentsFromNetworkAndSaveToDb(currentPortfolioId: Long, startDate: String, endDate: String): List<PaymentDbModel> {
        val securities = securityDao.getSecurityPackagesForPortfolio(currentPortfolioId)
        val paymentsFromNetwork = getPaymentsFromNetwork(securities, startDate, endDate)
        val payments = paymentsFromNetwork.map {
            PaymentDbModel.from(currentPortfolioId, it)
        }
        paymentDao.insert(payments)
        return paymentDao.getPaymentsWithSecurity(currentPortfolioId, startDate, endDate)
    }

    fun setSelectedYear(selectedYear: String) {
        paymentsPreferences.edit {
            putString(PREF_SELECTED_YEAR, selectedYear)
        }
    }

    fun getSelectedYear(): String {
        return paymentsPreferences.getString(PREF_SELECTED_YEAR, DateFormatter.getCurrentYear())!!
    }

    private suspend fun getPaymentsFromNetwork(securities: List<SecurityDbModel>, startDate: String, endDate: String): List<PaymentNetModel.Response> {
        val tickers = securities.map { it.ticker }
        val body = PaymentNetModel.Request(tickers, startDate, endDate)
        return divCalendarApi.fetchPayments(body)
    }
}