package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel
import com.infinity_coder.divcalendar.data.repositories.PaymentRepository
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import com.infinity_coder.divcalendar.domain.models.MonthlyPayment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class CalendarInteractor {

    private val dateFormat = DateFormatter.basicDateFormat

    suspend fun getPayments(): Flow<List<MonthlyPayment>> {
        return PaymentRepository.loadAllPayments().map { groupAndSortPayments(it) }
    }

    private fun groupAndSortPayments(payments: List<PaymentNetworkModel>): List<MonthlyPayment> {
        return payments.groupByDate()
            .map { MonthlyPayment.from(it) }
            .sortedBy { it.month }
    }

    private fun List<PaymentNetworkModel>.groupByDate(): List<Pair<Int, List<PaymentNetworkModel>>> {
        return groupBy {
            Calendar.getInstance().apply { time = dateFormat.parse(it.date) ?: Date() }
                .get(Calendar.MONTH)
        }.toList()
    }
}