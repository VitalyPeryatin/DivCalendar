package com.infinity_coder.divcalendar.domain.models

import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel

data class MonthlyPayment(
    val month: Int,
    val payments: List<PaymentNetworkModel>
) {
    companion object {
        fun from(paymentNetworkModel: Pair<Int, List<PaymentNetworkModel>>) =
            MonthlyPayment(
                month = paymentNetworkModel.first,
                payments = paymentNetworkModel.second
            )
    }
}