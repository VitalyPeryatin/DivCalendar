package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel

data class FooterPaymentPresentationModel(
    val id: Int,
    val income: Double,
    val originalCurrency: String,
    var currentCurrency: String
) : IComparableItem {

    companion object {
        fun from(groupPayment: Pair<String, List<PaymentNetworkModel>>) =
            FooterPaymentPresentationModel(
                id = groupPayment.first.toInt(),
                income = groupPayment.second.sumByDouble { payment -> payment.dividends },
                originalCurrency = groupPayment.second.first().currency,
                currentCurrency = ""
            )
    }

    override fun id() = id

    override fun content() = this
}