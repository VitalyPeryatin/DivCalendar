package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel

data class PaymentPresentationModel(
    val name: String,
    val logo: String,
    val count: Int,
    val dividends: Double,
    val date: String
) : IComparableItem {

    companion object {
        fun from(groupPayment: Pair<String, List<PaymentNetworkModel>>) =
            groupPayment.second.map { from(it) }

        private fun from(payment: PaymentNetworkModel) =
            PaymentPresentationModel(
                name = payment.name,
                logo = payment.logo,
                count = payment.count,
                dividends = payment.dividends,
                date = payment.date
            )
    }

    override fun id() = name

    override fun content() = this
}