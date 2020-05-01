package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel
import com.infinity_coder.divcalendar.data.repositories.PaymentRepository
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import com.infinity_coder.divcalendar.domain._common.convertStingToDate
import com.infinity_coder.divcalendar.domain._common.getNowDate
import com.infinity_coder.divcalendar.presentation.calendar.models.EditPaymentParams
import com.infinity_coder.divcalendar.presentation.calendar.models.MonthlyPayment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class PaymentInteractor {

    private val dateFormat = DateFormatter.basicDateFormat

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getPayments(year: String, isIncludeTaxes: Boolean): Flow<List<MonthlyPayment>> {
        return PaymentRepository.getPayments(
            "$year-01-01", "$year-12-31"
        )
            .map { calculateTaxesIfNeed(isIncludeTaxes, it) }
            .map { groupAndSortPayments(it) }
    }

    private fun calculateTaxesIfNeed(isIncludeTaxes: Boolean, payments: List<PaymentDbModel>): List<PaymentDbModel> {
        if (isIncludeTaxes) {
            payments.forEach { it.dividends *= TAX_FACTOR }
        }
        return payments
    }

    suspend fun updatePastPayment(editPaymentParams: EditPaymentParams) {
        val paymentDate = convertStingToDate(editPaymentParams.date)
        if (paymentDate.before(getNowDate())) {
            val payment = getPayment(
                editPaymentParams.portfolioId,
                editPaymentParams.isin,
                editPaymentParams.date
            ).apply {
                this.count = editPaymentParams.count
            }
            PaymentRepository.updatePayment(payment)
        }
    }

    private suspend fun getPayment(portfolioId: Long, isin: String, date: String): PaymentDbModel {
        return PaymentRepository.getPayment(portfolioId, isin, date)
    }

    fun setSelectedYear(selectedYear: String) {
        PaymentRepository.setSelectedYear(selectedYear)
    }

    fun getSelectedYear(): String {
        return PaymentRepository.getSelectedYear()
    }

    private fun groupAndSortPayments(payments: List<PaymentDbModel>): List<MonthlyPayment> {
        return payments.groupByDate()
            .map { MonthlyPayment.from(it) }
            .sortedBy {
                it.payments = sortedMonthPayments(it.payments)
                it.month
            }
    }

    private fun sortedMonthPayments(payments: List<PaymentDbModel>): List<PaymentDbModel> {
        val comparator = compareBy<PaymentDbModel> {
            convertStingToDate(it.date).time
        }
            .thenBy { it.security?.name }
            .thenByDescending { it.dividends }

        return payments.sortedWith(comparator)
    }

    private fun List<PaymentDbModel>.groupByDate(): List<Pair<Int, List<PaymentDbModel>>> {
        return groupBy {
            Calendar.getInstance().apply {
                time = dateFormat.parse(it.date) ?: Date()
            }.get(Calendar.MONTH)
        }.toList()
    }

    companion object {
        private const val TAX_FACTOR = 0.87
    }
}