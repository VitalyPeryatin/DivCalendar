package com.infinity_coder.divcalendar.data.repositories

import android.content.Context
import androidx.core.content.edit
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.PaymentNetModel
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import com.infinity_coder.divcalendar.domain._common.isExpiredDate
import com.infinity_coder.divcalendar.presentation.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

object PaymentRepository {

    private const val FIRST_DAY_OF_YEAR = "01-01"
    private const val LAST_DAY_OF_YEAR = "12-31"

    private const val PAYMENTS_PREF_NAME = "Payments"
    private const val PREF_SELECTED_YEAR = "selected_year"
    private val paymentsPreferences = App.instance.getSharedPreferences(PAYMENTS_PREF_NAME, Context.MODE_PRIVATE)

    private val securityDao = DivCalendarDatabase.roomDatabase.securityDao
    private val paymentDao = DivCalendarDatabase.roomDatabase.paymentDao

    private val divCalendarApi
        get() = RetrofitService.divCalendarApi

    suspend fun getPaymentsForSelectedYear(portfolioId: Long, selectedYear: String): Flow<List<PaymentDbModel>> = flow {
        val startDate = "$selectedYear-$FIRST_DAY_OF_YEAR"
        val endDate = "$selectedYear-$LAST_DAY_OF_YEAR"

        val cachedPayments = paymentDao.getPaymentsWithSecurity(portfolioId, startDate, endDate)
        if (cachedPayments.isNotEmpty())
            emit(cachedPayments)

        updatePaymentsInDatabase(portfolioId)
        emit(paymentDao.getPaymentsWithSecurity(portfolioId, startDate, endDate))
    }

    suspend fun updatePaymentsInDatabase(currentPortfolioId: Long) {
        val rightBorderLastYear = "${Calendar.getInstance().get(Calendar.YEAR)}-$FIRST_DAY_OF_YEAR"

        val payments = getPaymentsFromNetwork(currentPortfolioId).map {
            PaymentDbModel.from(currentPortfolioId, it)
        }

        paymentDao.deletePaymentsByDate(currentPortfolioId, rightBorderLastYear)
        paymentDao.insert(payments)
    }

    private suspend fun getPaymentsFromNetwork(currentPortfolioId: Long): List<PaymentNetModel.Response> {
        val tickers = securityDao.getSecurityPackagesForPortfolio(currentPortfolioId).map { it.ticker }

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val startDate = "$currentYear-$FIRST_DAY_OF_YEAR"
        val endDate = "${currentYear + 1}-$LAST_DAY_OF_YEAR"

        val body = PaymentNetModel.Request(tickers, startDate, endDate)
        return divCalendarApi.fetchPayments(body)
    }

    suspend fun getCachedPaymentsThatHaveNotExpired(portfolioId: Long): List<PaymentDbModel> {
        return paymentDao.getAllPaymentsWithSecurity(portfolioId).filterNot { isExpiredDate(it.date) }
    }

    suspend fun getAllCachedPayments(portfolioId: Long): List<PaymentDbModel> {
        return paymentDao.getAllPaymentsWithSecurity(portfolioId)
    }

    suspend fun updatePayment(payment: PaymentDbModel) {
        paymentDao.updatePayment(payment)
    }

    suspend fun getPayment(portfolioId: Long, isin: String, date: String): PaymentDbModel {
        return paymentDao.getPayment(portfolioId, isin, date)
    }

    fun setSelectedYear(selectedYear: String) {
        paymentsPreferences.edit {
            putString(PREF_SELECTED_YEAR, selectedYear)
        }
    }

    fun getSelectedYear(): String {
        return paymentsPreferences.getString(PREF_SELECTED_YEAR, DateFormatter.getCurrentYear())!!
    }
}