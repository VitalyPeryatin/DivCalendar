package com.infinity_coder.divcalendar.data.repositories

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.*
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import com.infinity_coder.divcalendar.domain.models.Payment
import com.infinity_coder.divcalendar.presentation.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

object PaymentRepository {

    private val divCalendarApi = RetrofitService.divCalendarApi

    private const val PAYMENTS_PREF_NAME = "Payments"
    private const val SELECTED_YEAR_NAME_KEY = "selected_year"
    private val paymentsPreferences = App.instance.getSharedPreferences(PAYMENTS_PREF_NAME, Context.MODE_PRIVATE)

    suspend fun getPayments(startDate: String, endDate: String): Flow<List<Payment>> = flow {
        val paymentsFromNetwork = getPaymentsFromNetwork(startDate, endDate)
        val securities = getSecuritiesForCurrentPortfolio()
        val payments = paymentsFromNetwork.map {payment ->
            Payment.from(payment, securities.find { payment.ticker == it.secid }!!)
        }
        emit(payments)
    }

    fun setSelectedYear(selectedYear:String){
        paymentsPreferences.edit {
            putString(SELECTED_YEAR_NAME_KEY, selectedYear)
        }
    }

    fun getSelectedYear():String{
        return paymentsPreferences.getString(SELECTED_YEAR_NAME_KEY, DateFormatter.getCurrentYear())!!
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