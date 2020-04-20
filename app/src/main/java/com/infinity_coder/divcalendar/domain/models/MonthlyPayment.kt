package com.infinity_coder.divcalendar.domain.models

import com.infinity_coder.divcalendar.data.db.model.PaymentDbModel

data class MonthlyPayment(
    val month: Int,
    val payments: List<PaymentDbModel>
) {
    companion object {
        fun from(monthsWithPayments: Pair<Int, List<PaymentDbModel>>) =
            MonthlyPayment(
                month = monthsWithPayments.first,
                payments = monthsWithPayments.second
            )
    }
}