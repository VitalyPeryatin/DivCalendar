package com.infinity_coder.divcalendar.presentation.tabs.calendar.models

import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel

data class MonthlyPayment(
    val month: Int,
    var payments: List<PaymentDbModel>
) {
    companion object {
        fun from(monthsWithPayments: Pair<Int, List<PaymentDbModel>>) =
            MonthlyPayment(
                month = monthsWithPayments.first, payments = monthsWithPayments.second
            )
    }
}