package com.infinity_coder.divcalendar.presentation.tabs.calendar.models

import com.example.delegateadapter.delegate.diff.IComparableItem

data class HeaderPaymentPresentationModel(
    val month: Int
) : IComparableItem {

    companion object {
        fun from(monthlyPayments: MonthlyPayment) =
            HeaderPaymentPresentationModel(
                month = monthlyPayments.month
            )
    }

    override fun id() = this

    override fun content() = this
}