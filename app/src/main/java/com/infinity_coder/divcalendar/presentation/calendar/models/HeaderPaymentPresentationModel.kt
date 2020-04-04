package com.infinity_coder.divcalendar.presentation.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel

data class HeaderPaymentPresentationModel(
    val month: Int
) : IComparableItem {

    companion object {
        fun from(groupPayment: Pair<String, List<PaymentNetworkModel>>) =
            HeaderPaymentPresentationModel(
                month = groupPayment.first.toInt()
            )
    }

    override fun id() = this

    override fun content() = this
}