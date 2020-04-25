package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel
import com.infinity_coder.divcalendar.data.repositories.PaymentRepository
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import com.infinity_coder.divcalendar.domain._common.convertStingToDate
import com.infinity_coder.divcalendar.domain._common.getNowDate
import com.infinity_coder.divcalendar.domain.models.MonthlyPayment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class CalendarInteractor {

    private val dateFormat = DateFormatter.basicDateFormat

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getPayments(year: String, isIncludeTaxes: Boolean): Flow<List<MonthlyPayment>> {
        return PaymentRepository.getPayments(
            "$year-01-01",
            "$year-12-31"
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

    suspend fun updatePastPayment(payment: PaymentDbModel) {
        val paymentDate = convertStingToDate(payment.date)
        if (getNowDate().before(paymentDate)) {
            PaymentRepository.updatePayment(payment)
        }
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
            .sortedBy { it.month }
    }

    private fun List<PaymentDbModel>.groupByDate(): List<Pair<Int, List<PaymentDbModel>>> {
        return groupBy {
            Calendar.getInstance().apply { time = dateFormat.parse(it.date) ?: Date() }
                .get(Calendar.MONTH)
        }.toList()
    }

    companion object {
        private const val TAX_FACTOR = 0.87
    }
}