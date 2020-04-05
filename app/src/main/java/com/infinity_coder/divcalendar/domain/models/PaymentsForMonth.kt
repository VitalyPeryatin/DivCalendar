package com.infinity_coder.divcalendar.domain.models

import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel

data class PaymentsForMonth(
    val month: Int,
    val payments: List<PaymentNetworkModel>
) {
    companion object {
        fun from(paymentsForSpecificMonth: Pair<Int, List<PaymentNetworkModel>>) =
            PaymentsForMonth(
                month = paymentsForSpecificMonth.first,
                payments = paymentsForSpecificMonth.second
            )
    }
}