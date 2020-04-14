package com.infinity_coder.divcalendar.domain.models

data class MonthlyPayment(
    val month: Int,
    val payments: List<Payment>
) {
    companion object {
        fun from(monthsWithPayments: Pair<Int, List<Payment>>) =
            MonthlyPayment(
                month = monthsWithPayments.first,
                payments = monthsWithPayments.second
            )
    }
}