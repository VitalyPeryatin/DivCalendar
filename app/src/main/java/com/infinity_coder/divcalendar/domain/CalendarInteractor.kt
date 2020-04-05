package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.repositories.PaymentRepository
import com.infinity_coder.divcalendar.domain.models.PaymentsForMonth
import java.text.SimpleDateFormat
import java.util.*

class CalendarInteractor {
    suspend fun getPayments(): List<PaymentsForMonth> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return PaymentRepository.loadAllPayments()
            .groupBy {
                val date = dateFormat.parse(it.date) ?: Date()
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.get(Calendar.MONTH)
            }
            .toList()
            .sortedBy { it.first }
            .map { PaymentsForMonth.from(it) }
    }
}