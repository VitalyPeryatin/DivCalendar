package com.infinity_coder.divcalendar.domain.models

import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel

data class MonthlyPayment(
    val month: Int,
    val payments: List<Payment>
) {
    companion object {
        fun from(monthsWithPayments: Pair<Int, List<PaymentNetworkModel>>) =
            MonthlyPayment(
                month = monthsWithPayments.first,
                payments = monthsWithPayments.second.map { Payment.from(it) }
            )
    }
}